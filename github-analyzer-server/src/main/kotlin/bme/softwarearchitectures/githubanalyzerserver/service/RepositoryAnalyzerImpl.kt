package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.dto.*
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.CommitsByMonth
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DeveloperCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DevelopmentCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.model.Commit
import org.springframework.stereotype.Service

@Service
class RepositoryAnalyzerImpl : RepositoryAnalyzer {

    override fun generateContributionResponse(commits: Collection<Commit>): ContributionResponse {
        val commitsByDevelopers = mutableListOf<CommitsByDeveloper>()

        commits.groupBy { it.author }
                .forEach { (authorName, commits) -> commitsByDevelopers.add(CommitsByDeveloper(authorName, commits.size)) }
        return ContributionResponse(commits.size, commitsByDevelopers)
    }

    override fun generateModificationResponse(commits: Collection<Commit>): ModificationResponse {
        val modificationsByDate = mutableListOf<ModificationsByDate>()

        commits.groupBy { "${it.created.year}|${it.created.month.value - 1}" }
                .forEach { (date, commits) ->
                    var linesAdded = 0
                    var linesDeleted = 0
                    commits.forEach { commit ->
                        linesAdded += commit.linesAdded
                        linesDeleted += commit.linesDeleted
                    }
                    val splitDate = date.split("|")
                    modificationsByDate.add(ModificationsByDate(splitDate[0].toInt(), splitDate[1].toInt(), linesAdded, linesDeleted))
                }
        return ModificationResponse(modificationsByDate)
    }

    override fun generateDistributionResponse(commits: Collection<Commit>): DistributionResponse {
        val byMonth = mutableListOf<AverageCommitsByMonth>()
        val byDay = mutableListOf<AverageCommitsByDay>()
        val byPeriods = mutableListOf<AverageCommitsByDayPeriods>()

        val averageCommitsByMonth = mutableMapOf<Int, MutableList<Int>>()
        for (i in 0..11) averageCommitsByMonth[i] = mutableListOf()
        commits.groupBy { "${it.created.year}|${it.created.month.value - 1}" }
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
        commits.groupBy { "${it.created.year}|${it.created.month.value - 1}|${it.created.dayOfWeek.value - 1}" }
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
            "${it.created.year}|${it.created.month.value - 1}|${it.created.dayOfWeek.value - 1}" +
                    "|${it.created.hour}"
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

    override fun generateDevelopmentCompareResponse(commits1: Collection<Commit>, commits2: Collection<Commit>): DevelopmentCompareResponse {
        val repository1Developers = commits1.groupBy { it.author }.count()
        val repository2Developers = commits2.groupBy { it.author }.count()

        val repository1CommitsByMonth = getCommitsByMonthOfLastYear(commits1).fillEmpty().sortedBy { it.month }
        val repository2CommitsByMonth = getCommitsByMonthOfLastYear(commits2).fillEmpty().sortedBy { it.month }

        return DevelopmentCompareResponse(repository1Developers, repository2Developers, repository1CommitsByMonth, repository2CommitsByMonth)
    }

    override fun generateDeveloperCompareResponse(commits1: Collection<Commit>, commits2: Collection<Commit>): DeveloperCompareResponse {
        val developer1Name = commits1.groupBy { it.author }.maxBy { (_, commits) -> commits.count() }!!.key
        val developer2Name = commits2.groupBy { it.author }.maxBy { (_, commits) -> commits.count() }!!.key

        val developer1CommitsByMonth = getCommitsByMonthOfLastYear(commits1, developer1Name).fillEmpty().sortedBy { it.month }
        val developer2CommitsByMonth = getCommitsByMonthOfLastYear(commits2, developer2Name).fillEmpty().sortedBy { it.month }

        return DeveloperCompareResponse(developer1Name, developer2Name, developer1CommitsByMonth, developer2CommitsByMonth)
    }

    private fun getCommitsByMonthOfLastYear(commits: Collection<Commit>, developerName: String = ""): MutableList<CommitsByMonth> {
        val lastYear = commits.maxBy { it.created.year }!!.created.year

        val commitsByMonth = mutableListOf<CommitsByMonth>()
        commits.groupBy { "${it.created.year}|${it.created.month.value}" }
                .forEach { (date, commits) ->
                    val splitDate = date.split("|")
                    val year = splitDate[0].toInt()
                    if (year == lastYear && developerName == "") {
                        commitsByMonth.add(CommitsByMonth(year, splitDate[1].toInt(), commits.count()))
                    } else if (year == lastYear && developerName != "" && commits.isNotEmpty()) {
                        val count = commits.filter { it.author == developerName }.count()
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