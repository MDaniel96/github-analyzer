package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.dto.*
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.CommitsByMonth
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DeveloperCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DevelopmentCompareResponse
import org.kohsuke.github.GHCommit
import org.springframework.stereotype.Service

@Service
class RepositoryAnalyzerImpl : RepositoryAnalyzer {

    override fun generateContributionResponse(commits: Array<GHCommit>): ContributionResponse {
        val commitsByDevelopers = mutableListOf<CommitsByDeveloper>()

        commits.groupBy { it.commitShortInfo.author.name }
                .forEach { (authorName, commits) -> commitsByDevelopers.add(CommitsByDeveloper(authorName, commits.size)) }
        return ContributionResponse(commits.size, commitsByDevelopers)
    }

    override fun generateModificationResponse(commits: Array<GHCommit>): ModificationResponse {
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

    override fun generateDistributionResponse(commits: Array<GHCommit>): DistributionResponse {
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

    override fun generateDevelopmentCompareResponse(commits1: Array<GHCommit>, commits2: Array<GHCommit>): DevelopmentCompareResponse {
        val repository1Developers = commits1.groupBy { it.commitShortInfo.author.name }.count()
        val repository2Developers = commits2.groupBy { it.commitShortInfo.author.name }.count()

        val repository1CommitsByMonth = getCommitsByMonthOfLastYear(commits1).fillEmpty().sortedBy { it.month }
        val repository2CommitsByMonth = getCommitsByMonthOfLastYear(commits2).fillEmpty().sortedBy { it.month }

        return DevelopmentCompareResponse(repository1Developers, repository2Developers, repository1CommitsByMonth, repository2CommitsByMonth)
    }

    override fun generateDeveloperCompareResponse(commits1: Array<GHCommit>, commits2: Array<GHCommit>): DeveloperCompareResponse {
        val developer1Name = commits1.groupBy { it.commitShortInfo.author.name }.maxBy { (_, commits) -> commits.count() }!!.key
        val developer2Name = commits2.groupBy { it.commitShortInfo.author.name }.maxBy { (_, commits) -> commits.count() }!!.key

        val developer1CommitsByMonth = getCommitsByMonthOfLastYear(commits1, developer1Name).fillEmpty().sortedBy { it.month }
        val developer2CommitsByMonth = getCommitsByMonthOfLastYear(commits2, developer2Name).fillEmpty().sortedBy { it.month }

        return DeveloperCompareResponse(developer1Name, developer2Name, developer1CommitsByMonth, developer2CommitsByMonth)
    }

    private fun getCommitsByMonthOfLastYear(commits: Array<GHCommit>, developerName: String = ""): MutableList<CommitsByMonth> {
        val lastYear = 1900 + commits.maxBy { it.commitShortInfo.commitDate.year }!!.commitShortInfo.commitDate.year

        val commitsByMonth = mutableListOf<CommitsByMonth>()
        commits.groupBy { "${it.commitShortInfo.commitDate.year}|${it.commitShortInfo.commitDate.month}" }
                .forEach { (date, commits) ->
                    val splitDate = date.split("|")
                    val year = 1900 + splitDate[0].toInt()
                    if (year == lastYear && developerName == "") {
                        commitsByMonth.add(CommitsByMonth(year, splitDate[1].toInt(), commits.count()))
                    } else if (year == lastYear && developerName != "" && commits.isNotEmpty()) {
                        val count = commits.filter { it.commitShortInfo.author.name == developerName }.count()
                        commitsByMonth.add(CommitsByMonth(year, splitDate[1].toInt(), count))
                    }
                }
        return commitsByMonth
    }

    fun MutableList<CommitsByMonth>.fillEmpty(): MutableList<CommitsByMonth> {
        val year = this[0].year
        val filledMonths = this.map { it.month }.toSet()
        for (month in 0..11) {
            if (!filledMonths.contains(month)) {
                this.add(CommitsByMonth(year = year, month = month, commits = 0))
            }
        }
        return this
    }
}