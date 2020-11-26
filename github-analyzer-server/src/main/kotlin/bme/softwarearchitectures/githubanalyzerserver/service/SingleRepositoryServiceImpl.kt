package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.config.AppConfig
import bme.softwarearchitectures.githubanalyzerserver.dto.ContributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.DistributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.ModificationResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.SingleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.dto.mappers.toCommitList
import bme.softwarearchitectures.githubanalyzerserver.dto.mappers.toDate
import bme.softwarearchitectures.githubanalyzerserver.model.Commit
import bme.softwarearchitectures.githubanalyzerserver.model.Repository
import bme.softwarearchitectures.githubanalyzerserver.repository.CommitRepository
import bme.softwarearchitectures.githubanalyzerserver.repository.RepositoryRepository
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GitHub
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SingleRepositoryServiceImpl(
        val appConfig: AppConfig,
        val repositoryAnalyzer: RepositoryAnalyzer,
        val repositoryRepository: RepositoryRepository,
        val commitRepository: CommitRepository
) : SingleRepositoryService {

    val contributionResultMap = mutableMapOf<SingleRepositoryRequest, ContributionResponse>()
    val modificationResultMap = mutableMapOf<SingleRepositoryRequest, ModificationResponse>()
    val distributionResultMap = mutableMapOf<SingleRepositoryRequest, DistributionResponse>()

    override fun analyze(request: SingleRepositoryRequest) {
        val gitHubAPI = connectGithub(request)
        val repository = gitHubAPI.getRepository(request.repositoryId)
        val repositoryHistory = repositoryRepository.findByUrl(request.repositoryUrl)

        if (repositoryHistory != null) {
            repositoryHistory.lastQueried = LocalDateTime.now()

            val newCommits = repository.queryCommits()
                    .since(repositoryHistory.lastQueried.toDate().time)
                    .list().toArray()
            if (newCommits.isNotEmpty()) {
                generateResponse(repositoryHistory.commits, request)
                saveCommits(newCommits, repositoryHistory)
            } else {
                // TODO: if newCommits empty --> cache data
            }
        } else {
            val ghCommits = repository.listCommits().toArray()
            val commits = saveRepositoryWithCommits(request.repositoryUrl, repository.name, ghCommits)
            generateResponse(commits, request)
        }
    }

    private fun generateResponse(commits: Collection<Commit>, request: SingleRepositoryRequest) {
        contributionResultMap[request] = repositoryAnalyzer.generateContributionResponse(commits)
        distributionResultMap[request] = repositoryAnalyzer.generateDistributionResponse(commits)
        modificationResultMap[request] = repositoryAnalyzer.generateModificationResponse(commits)
    }

    override fun getContributionResultMap(request: SingleRepositoryRequest) = contributionResultMap[request]

    override fun getModificationResultMap(request: SingleRepositoryRequest) = modificationResultMap[request]

    override fun getDistributionResultMap(request: SingleRepositoryRequest) = distributionResultMap[request]

    private fun connectGithub(request: SingleRepositoryRequest): GitHub {
        return GitHub.connectUsingOAuth(
                if (request.accessToken != "")
                    request.accessToken
                else
                    appConfig.accessToken
        )
    }

    private fun saveRepositoryWithCommits(repositoryUrl: String, repositoryName: String, commits: Array<GHCommit>): Collection<Commit> {
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

    private fun saveCommits(newCommits: Array<GHCommit>, repository: Repository) {
        val commitsToSave = newCommits.toCommitList(repository)
        repository.commits.addAll(commitsToSave)
        commitRepository.saveAll(commitsToSave)
    }
}