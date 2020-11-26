package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.config.AppConfig
import bme.softwarearchitectures.githubanalyzerserver.dto.ContributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.DistributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.ModificationResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.SingleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.model.Repository
import bme.softwarearchitectures.githubanalyzerserver.repository.RepositoryRepository
import org.kohsuke.github.GitHub
import org.springframework.stereotype.Service

@Service
class SingleRepositoryServiceImpl(
        val appConfig: AppConfig,
        val repositoryAnalyzer: RepositoryAnalyzer,
        val repositoryRepository: RepositoryRepository
) : SingleRepositoryService {

    val contributionResultMap = mutableMapOf<SingleRepositoryRequest, ContributionResponse>()
    val modificationResultMap = mutableMapOf<SingleRepositoryRequest, ModificationResponse>()
    val distributionResultMap = mutableMapOf<SingleRepositoryRequest, DistributionResponse>()

    override fun analyze(request: SingleRepositoryRequest) {

        repositoryRepository.save(
                Repository(
                        name = "test"
                )
        )

        val gitHubAPI = GitHub.connectUsingOAuth(
                if (request.accessToken != "")
                    request.accessToken
                else
                    appConfig.accessToken
        )

        val ghRepository = gitHubAPI.getRepository(request.repositoryId)
        ghRepository?.let { repository ->
            val commits = repository.listCommits().toArray()

            contributionResultMap[request] = repositoryAnalyzer.generateContributionResponse(commits)
            distributionResultMap[request] = repositoryAnalyzer.generateDistributionResponse(commits)
            modificationResultMap[request] = repositoryAnalyzer.generateModificationResponse(commits)
        }
    }

    override fun getContributionResultMap(request: SingleRepositoryRequest) = contributionResultMap[request]

    override fun getModificationResultMap(request: SingleRepositoryRequest) = modificationResultMap[request]

    override fun getDistributionResultMap(request: SingleRepositoryRequest) = distributionResultMap[request]
}