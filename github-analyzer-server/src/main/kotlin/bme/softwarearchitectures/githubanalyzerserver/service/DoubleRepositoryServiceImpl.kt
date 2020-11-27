package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.config.AppConfig
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DeveloperCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DevelopmentCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DoubleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.dto.mappers.toCommitList
import bme.softwarearchitectures.githubanalyzerserver.dto.mappers.toDate
import bme.softwarearchitectures.githubanalyzerserver.model.Commit
import bme.softwarearchitectures.githubanalyzerserver.repository.CommitRepository
import bme.softwarearchitectures.githubanalyzerserver.repository.RepositoryRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DoubleRepositoryServiceImpl(
        val appConfig: AppConfig,
        val repositoryAnalyzer: RepositoryAnalyzer,
        val repositoryRepository: RepositoryRepository,
        val commitRepository: CommitRepository,
        val singleRepositoryService: SingleRepositoryService
) : DoubleRepositoryService {

    val developmentCompareResultMap = mutableMapOf<DoubleRepositoryRequest, DevelopmentCompareResponse>()
    val developerCompareResultMap = mutableMapOf<DoubleRepositoryRequest, DeveloperCompareResponse>()

    override fun analyze(request: DoubleRepositoryRequest) {
        val commits1 = getCommits(request.access1Token, request.repository1Id, request.repository1Url)
        val commits2 = getCommits(request.access2Token, request.repository2Id, request.repository2Url)

        developmentCompareResultMap[request] = repositoryAnalyzer.generateDevelopmentCompareResponse(commits1, commits2)
        developerCompareResultMap[request] = repositoryAnalyzer.generateDeveloperCompareResponse(commits1, commits2)
    }

    override fun getDevelopmentCompareResult(request: DoubleRepositoryRequest) = developmentCompareResultMap.remove(request)

    override fun getDeveloperCompareResult(request: DoubleRepositoryRequest) = developerCompareResultMap.remove(request)

    private fun getCommits(accessToken: String?, repositoryId: String, repositoryUrl: String): Collection<Commit> {
        val gitHubAPI = singleRepositoryService.connectGithub(accessToken)
        val repository = gitHubAPI.getRepository(repositoryId)
        val repositoryHistory = repositoryRepository.findByUrl(repositoryUrl)

        if (repositoryHistory != null) {
            val newCommits = repository.queryCommits()
                    .since(repositoryHistory.lastQueried.toDate().time)
                    .list().toArray()

            repositoryHistory.lastQueried = LocalDateTime.now()
            if (newCommits.isNotEmpty()) {
                repositoryHistory.commits.addAll(newCommits.toCommitList(repositoryHistory))
                repositoryHistory.commits.sortedBy { it.created.month.value }
                singleRepositoryService.saveCommits(newCommits, repositoryHistory)
            }
            return repositoryHistory.commits
        } else {
            val ghCommits = repository.listCommits().toArray()
            return singleRepositoryService.saveRepositoryWithCommits(repositoryUrl, repository.name, ghCommits)
        }
    }
}
