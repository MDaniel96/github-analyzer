package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.config.AppConfig
import bme.softwarearchitectures.githubanalyzerserver.dto.ContributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.DistributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.ModificationResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.SingleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.dto.mappers.*
import bme.softwarearchitectures.githubanalyzerserver.model.Commit
import bme.softwarearchitectures.githubanalyzerserver.model.Repository
import bme.softwarearchitectures.githubanalyzerserver.repository.*
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GitHub
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SingleRepositoryServiceImpl(
        val appConfig: AppConfig,
        val repositoryAnalyzer: RepositoryAnalyzer,
        val repositoryRepository: RepositoryRepository,
        val commitRepository: CommitRepository,
        val contributionRepository: ContributionRepository,
        val modificationRepository: ModificationRepository,
        val distributionRepository: DistributionRepository
) : SingleRepositoryService {

    val contributionResultMap = mutableMapOf<SingleRepositoryRequest, ContributionResponse>()
    val modificationResultMap = mutableMapOf<SingleRepositoryRequest, ModificationResponse>()
    val distributionResultMap = mutableMapOf<SingleRepositoryRequest, DistributionResponse>()

    override fun analyze(request: SingleRepositoryRequest) {
        val gitHubAPI = connectGithub(request?.accessToken)
        val repository = gitHubAPI.getRepository(request.repositoryId)
        val repositoryHistory = repositoryRepository.findByUrl(request.repositoryUrl)

        if (repositoryHistory != null) {
            val newCommits = repository.queryCommits()
                    .since(repositoryHistory.lastQueried.toDate().time)
                    .list().toArray()

            repositoryHistory.lastQueried = LocalDateTime.now()
            if (newCommits.isNotEmpty()) {
                repositoryHistory.commits.addAll(newCommits.toCommitList(repositoryHistory))
                repositoryHistory.commits.sortedBy { it.created.month.value }
                generateResponse(repositoryHistory.commits, request)
                saveCommits(newCommits, repositoryHistory)
            } else {
                val contributions = contributionRepository.findByRepositoryUrl(repositoryHistory.url)
                contributionResultMap[request] = contributions.toContributionResponse()

                val modifications = modificationRepository.findByRepositoryUrl(repositoryHistory.url)
                modificationResultMap[request] = modifications.toModificationResponse()

                val distributions = distributionRepository.findByRepositoryUrl(repositoryHistory.url)
                distributionResultMap[request] = distributions.toDistributionResponse()
            }
        } else {
            val ghCommits = repository.listCommits().toArray()
            val commits = saveRepositoryWithCommits(request.repositoryUrl, repository.name, ghCommits)
            generateResponse(commits, request)
        }
    }

    private fun generateResponse(commits: Collection<Commit>, request: SingleRepositoryRequest) {
        contributionResultMap[request] = repositoryAnalyzer.generateContributionResponse(commits)
        saveContribution(contributionResultMap[request]!!, request.repositoryUrl)

        distributionResultMap[request] = repositoryAnalyzer.generateDistributionResponse(commits)
        saveDistribution(distributionResultMap[request]!!, request.repositoryUrl)

        modificationResultMap[request] = repositoryAnalyzer.generateModificationResponse(commits)
        saveModification(modificationResultMap[request]!!, request.repositoryUrl)
    }

    private fun saveContribution(contributionResponse: ContributionResponse, repositoryUrl: String) {
        val contributionsToDelete = contributionRepository.findByRepositoryUrl(repositoryUrl)
        contributionRepository.deleteAll(contributionsToDelete)
        val contributions = contributionResponse.toContributionList(repositoryUrl)
        contributionRepository.saveAll(contributions)
    }

    private fun saveDistribution(distributionResponse: DistributionResponse, repositoryUrl: String) {
        val distributionsToDelete = distributionRepository.findByRepositoryUrl(repositoryUrl)
        distributionRepository.deleteAll(distributionsToDelete)
        val distributions = distributionResponse.toDistributionList(repositoryUrl)
        distributionRepository.saveAll(distributions)
    }

    private fun saveModification(modificationResponse: ModificationResponse, repositoryUrl: String) {
        val modificationsToDelete = modificationRepository.findByRepositoryUrl(repositoryUrl)
        modificationRepository.deleteAll(modificationsToDelete)
        val modifications = modificationResponse.toModificationList(repositoryUrl)
        modificationRepository.saveAll(modifications)
    }

    override fun getContributionResultMap(request: SingleRepositoryRequest) = contributionResultMap.remove(request)

    override fun getModificationResultMap(request: SingleRepositoryRequest) = modificationResultMap.remove(request)

    override fun getDistributionResultMap(request: SingleRepositoryRequest) = distributionResultMap.remove(request)

    override fun connectGithub(accessToken: String?): GitHub {
        return GitHub.connectUsingOAuth(
                if (accessToken != null && accessToken != "")
                    accessToken
                else
                    appConfig.accessToken
        )
    }

    override fun saveRepositoryWithCommits(repositoryUrl: String, repositoryName: String, commits: Array<GHCommit>): Collection<Commit> {
        val repositoryToSave = repositoryRepository.save(
                Repository(
                        url = repositoryUrl,
                        name = repositoryName,
                        lastQueried = LocalDateTime.now()
                )
        )
        val commitsToSave = commits.toCommitList(repositoryToSave)
        commitRepository.saveAll(commitsToSave)
        repositoryToSave.commits = commitsToSave
        return commitsToSave
    }

    override fun saveCommits(newCommits: Array<GHCommit>, repository: Repository) {
        val commitsToSave = newCommits.toCommitList(repository)
        repository.commits.addAll(commitsToSave)
        commitRepository.saveAll(commitsToSave)
    }
}