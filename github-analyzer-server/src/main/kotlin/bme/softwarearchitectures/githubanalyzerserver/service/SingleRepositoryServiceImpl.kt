package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.model.*
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
            val modification = generateModificationResponse(commits)

            val result = SingleRepositoryResult(repository.name, contribution, modification)
            println(result)
        }
    }

    override fun getRepositoryInfo(repositoryUrl: String, accessToken: String): SingleRepositoryResult? {
        // TODO: return analyzed repository from memory db or null if not found
        return null
    }

    private fun generateContributionResponse(commits: Array<GHCommit>): ContributionResponse {
        val commitsByDevelopers = mutableListOf<CommitsByDeveloper>()
        commits.groupBy { it.commitShortInfo.author.name }
                .forEach { (authorName, commits) -> commitsByDevelopers.add(CommitsByDeveloper(authorName, commits.size)) }
        return ContributionResponse(commits.size, commitsByDevelopers)
    }

    private fun generateModificationResponse(commits: Array<GHCommit>): ModificationResponse {
        val modificationsByDate = mutableListOf<ModificationsByDate>()
        commits.groupBy { it.commitShortInfo.commitDate.month }
                .forEach { (month, commits) ->
                    val linesAdded = commits.sumBy { commit -> commit.linesAdded }
                    val linesDeleted = commits.sumBy { commit -> commit.linesDeleted }
                    modificationsByDate.add(ModificationsByDate(commits.get(0).commitDate.year, month, linesAdded, linesDeleted))
                }
        return ModificationResponse(modificationsByDate)
    }
}