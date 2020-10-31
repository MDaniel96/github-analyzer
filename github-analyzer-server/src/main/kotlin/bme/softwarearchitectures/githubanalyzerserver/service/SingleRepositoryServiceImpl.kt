package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.model.CommitsByDeveloper
import bme.softwarearchitectures.githubanalyzerserver.model.ContributionResponse
import bme.softwarearchitectures.githubanalyzerserver.model.SingleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.model.SingleRepositoryResult
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GitHub
import org.springframework.stereotype.Service

@Service
class SingleRepositoryServiceImpl : SingleRepositoryService {

    private val github = GitHub.connectAnonymously()

    override fun analyze(request: SingleRepositoryRequest) {
        // TODO: analyze repository and save it to memory db
        // TODO: analyze private repository

        val githubAPI = request.accessToken?.let {
            GitHub.connectUsingOAuth(request.accessToken)
        } ?: github

        val ghRepository = githubAPI.getRepository(request.repositoryId)
        ghRepository?.let { repository ->
            val commits = repository.listCommits().toArray()

            val contribution = generateContributionResponse(commits)

            val result = SingleRepositoryResult(repository.name, contribution)
        }
    }

    override fun getRepositoryInfo(repositoryUrl: String, accessToken: String): SingleRepositoryResult? {
        // TODO: return analyzed repository from memory db or null if not found
        return null
    }

    private fun generateContributionResponse(commits: Array<GHCommit>): ContributionResponse {
        val commitsByDevelopers = mutableListOf<CommitsByDeveloper>()
        commits.groupBy { it.commitShortInfo.author.name }
                .forEach {
                    commitsByDevelopers.add(CommitsByDeveloper(
                            developerName = it.key,
                            commits = it.value.size))
                }
        return ContributionResponse(commits.size, commitsByDevelopers)
    }
}