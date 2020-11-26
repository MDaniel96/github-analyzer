package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.config.AppConfig
import bme.softwarearchitectures.githubanalyzerserver.model.compare.DeveloperCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.model.compare.DevelopmentCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.model.compare.DoubleRepositoryRequest
import org.kohsuke.github.GitHub
import org.springframework.stereotype.Service

@Service
class DoubleRepositoryServiceImpl(
        val appConfig: AppConfig,
        val repositoryAnalyzer: RepositoryAnalyzer
) : DoubleRepositoryService {

    val developmentCompareResultMap = mutableMapOf<DoubleRepositoryRequest, DevelopmentCompareResponse>()
    val developerCompareResultMap = mutableMapOf<DoubleRepositoryRequest, DeveloperCompareResponse>()

    override fun analyze(request: DoubleRepositoryRequest) {
        val gitHubAPI1 = GitHub.connectUsingOAuth(
                if (request.access1Token != "")
                    request.access1Token
                else
                    appConfig.accessToken
        )

        val gitHubAPI2 = GitHub.connectUsingOAuth(
                if (request.access2Token != "")
                    request.access2Token
                else
                    appConfig.accessToken
        )

        val ghRepository1 = gitHubAPI1.getRepository(request.repository1Id)
        val ghRepository2 = gitHubAPI2.getRepository(request.repository2Id)

        if (ghRepository1 !== null && ghRepository2 !== null) {
            val commits1 = ghRepository1.listCommits().toArray()
            val commits2 = ghRepository2.listCommits().toArray()

            developmentCompareResultMap[request] = repositoryAnalyzer.generateDevelopmentCompareResponse(commits1, commits2)
            developerCompareResultMap[request] = repositoryAnalyzer.generateDeveloperCompareResponse(commits1, commits2)
        }
    }

    override fun getDevelopmentCompareResult(request: DoubleRepositoryRequest) = developmentCompareResultMap[request]

    override fun getDeveloperCompareResult(request: DoubleRepositoryRequest) = developerCompareResultMap[request]
}
