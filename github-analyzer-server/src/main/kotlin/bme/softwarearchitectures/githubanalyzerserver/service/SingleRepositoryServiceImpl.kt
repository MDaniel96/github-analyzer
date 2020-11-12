package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.model.*
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GitHub
import org.springframework.stereotype.Service

@Service
class SingleRepositoryServiceImpl : SingleRepositoryService {

    val singleRepositoryResultMap = mutableMapOf<SingleRepositoryRequest, SingleRepositoryResult>()

    private val github = GitHub.connectAnonymously()

    override fun analyze(request: SingleRepositoryRequest) {
        val githubAPI = request.accessToken?.let {
            GitHub.connectUsingOAuth(request.accessToken)
        } ?: github

        val ghRepository = githubAPI.getRepository(request.repositoryId)
        ghRepository?.let { repository ->
            val commits = repository.listCommits().toArray()

            val contribution = generateContributionResponse(commits)
            val modification = generateModificationResponse(commits)
            val distribution = generateDistributionResponse(commits)

            val result = SingleRepositoryResult(repository.name, contribution, modification, distribution)
            singleRepositoryResultMap.put(request, result)
        }
    }

    override fun getRepositoryInfo(request: SingleRepositoryRequest) = singleRepositoryResultMap[request]

    private fun generateContributionResponse(commits: Array<GHCommit>): ContributionResponse {
        val commitsByDevelopers = mutableListOf<CommitsByDeveloper>()
        commits.groupBy { it.commitShortInfo.author.name }
                .forEach { (authorName, commits) -> commitsByDevelopers.add(CommitsByDeveloper(authorName, commits.size)) }
        return ContributionResponse(commits.size, commitsByDevelopers)
    }

    private fun generateModificationResponse(commits: Array<GHCommit>): ModificationResponse {
        val modificationsByDate = mutableListOf<ModificationsByDate>()

        commits.groupBy { "${it.commitShortInfo.commitDate.year}|${it.commitShortInfo.commitDate.month}" }
                .forEach { (date, commits) ->
                    var linesAdded = 0
                    var linesDeleted = 0
                    commits.forEach { commit ->
                        linesAdded += commit.linesAdded
                        linesDeleted += commit.linesDeleted
                    }
                    val splitDate = date.split("|")
                    modificationsByDate.add(ModificationsByDate(1900 + splitDate[0].toInt(), splitDate[1].toInt(), linesAdded, linesDeleted))
                }
        return ModificationResponse(modificationsByDate)
    }

    private fun generateDistributionResponse(commits: Array<GHCommit>): DistributionResponse {
        val byMonth = mutableListOf<AverageCommitsByMonth>()
        val byDay = mutableListOf<AverageCommitsByDay>()
        val byPeriods = mutableListOf<AverageCommitsByDayPeriods>()

        val averageCommitsByMonth = mutableMapOf<Int, MutableList<Int>>()
        for (i in 0..11) averageCommitsByMonth[i] = mutableListOf()
        commits.groupBy { "${it.commitShortInfo.commitDate.year}|${it.commitShortInfo.commitDate.month}" }
                .forEach { (date, commits) ->
                    val splitDate = date.split("|")
                    val month = splitDate[1]
                    averageCommitsByMonth[month.toInt()]?.add(commits.size)
                }
        averageCommitsByMonth.forEach { (month, commitNumberList) ->
            if (commitNumberList.isNotEmpty()) {
                byMonth.add(AverageCommitsByMonth(month, commitNumberList.average()))
            }
        }

        val averageCommitsByDay = mutableMapOf<Int, MutableList<Int>>()
        for (i in 0..6) averageCommitsByDay[i] = mutableListOf()
        commits.groupBy { "${it.commitShortInfo.commitDate.year}|${it.commitShortInfo.commitDate.month}|${it.commitShortInfo.commitDate.day}" }
                .forEach { (date, commits) ->
                    val splitDate = date.split("|")
                    val day = splitDate[2]
                    averageCommitsByDay[day.toInt()]?.add(commits.size)
                }
        averageCommitsByDay.forEach { (day, commitNumberList) ->
            if (commitNumberList.isNotEmpty()) {
                byDay.add(AverageCommitsByDay(day, commitNumberList.average()))
            }
        }

        val averageCommitsByPeriods = mutableMapOf<String, MutableList<Int>>()
        averageCommitsByPeriods["0-6"] = mutableListOf()
        averageCommitsByPeriods["7-12"] = mutableListOf()
        averageCommitsByPeriods["13-18"] = mutableListOf()
        averageCommitsByPeriods["19-24"] = mutableListOf()
        commits.groupBy {
            "${it.commitShortInfo.commitDate.year}|${it.commitShortInfo.commitDate.month}|${it.commitShortInfo.commitDate.day}" +
                    "|${it.commitShortInfo.commitDate.hours}"
        }
                .forEach { (date, commits) ->
                    val splitDate = date.split("|")
                    val hour = splitDate[3]
                    val period = when (hour.toInt()) {
                        in 0..6 -> "0-6"
                        in 7..12 -> "7-12"
                        in 13..18 -> "13-18"
                        in 19..24 -> "19-24"
                        else -> "not_found"
                    }
                    averageCommitsByPeriods[period]?.add(commits.size)
                }
        averageCommitsByPeriods.forEach { (period, commitNumberList) ->
            if (commitNumberList.isNotEmpty()) {
                byPeriods.add(AverageCommitsByDayPeriods(period, commitNumberList.average()))
            }
        }

        return DistributionResponse(byMonth, byDay, byPeriods)
    }
}