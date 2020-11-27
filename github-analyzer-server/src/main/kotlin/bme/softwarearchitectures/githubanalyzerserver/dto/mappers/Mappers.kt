package bme.softwarearchitectures.githubanalyzerserver.dto.mappers

import bme.softwarearchitectures.githubanalyzerserver.dto.*
import bme.softwarearchitectures.githubanalyzerserver.model.*
import org.kohsuke.github.GHCommit
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

fun GHCommit.toCommit(repository: Repository) = Commit(
        author = commitShortInfo.author.name,
        created = commitShortInfo.commitDate.toLocalDateTime(),
        linesAdded = linesAdded,
        linesDeleted = linesDeleted,
        repository = repository
)

fun Array<GHCommit>.toCommitList(repository: Repository) = this.map { it.toCommit(repository) }.toMutableSet()

fun Date.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.time), ZoneOffset.UTC)

fun LocalDateTime.toDate(): Date = Date.from(this.atZone(ZoneId.systemDefault()).toInstant())

fun ContributionResponse.toContributionList(repositoryUrl: String): List<Contribution> =
        this.commitsByDevelopers.map {
            Contribution(
                    developerName = it.developerName,
                    commits = it.commits,
                    totalCommits = totalCommits,
                    repositoryUrl = repositoryUrl
            )
        }.toList()

fun List<Contribution>.toContributionResponse() =
        ContributionResponse(
                totalCommits = this[0].totalCommits,
                commitsByDevelopers = this.map { CommitsByDeveloper(it.developerName, it.commits) }.toList()
        )

fun ModificationResponse.toModificationList(repositoryUrl: String): List<Modification> =
        this.modificationsByDate.map {
            Modification(
                    year = it.year,
                    month = it.month,
                    addedLines = it.addedLines,
                    removedLines = it.removedLines,
                    repositoryUrl = repositoryUrl
            )
        }.toList()

fun List<Modification>.toModificationResponse() =
        ModificationResponse(
                modificationsByDate = this.map { ModificationsByDate(it.year, it.month, it.addedLines, it.removedLines) }.toList()
        )

fun DistributionResponse.toDistributionList(repositoryUrl: String): List<Distribution> {
    val averageMonthList = this.averageCommitsByMonth.map {
        Distribution(
                type = Distribution.Type.MONTH,
                time = it.month.toString(),
                commits = it.commits,
                repositoryUrl = repositoryUrl
        )
    }.toList()

    val averageDayList = this.averageCommitsByDay.map {
        Distribution(
                type = Distribution.Type.DAY,
                time = it.day.toString(),
                commits = it.commits,
                repositoryUrl = repositoryUrl
        )
    }.toList()

    val averagePeriodList = this.averageCommitsByDayPeriods.map {
        Distribution(
                type = Distribution.Type.PERIOD,
                time = it.period,
                commits = it.commits,
                repositoryUrl = repositoryUrl
        )
    }.toList()

    return averageMonthList.union(averageDayList.union(averagePeriodList)).toList()
}

fun List<Distribution>.toDistributionResponse() =
        DistributionResponse(
                averageCommitsByMonth = this.filter { it.type == Distribution.Type.MONTH }
                        .map { AverageCommitsByMonth(it.time.toInt(), it.commits) }.toList(),

                averageCommitsByDay = this.filter { it.type == Distribution.Type.DAY }
                        .map { AverageCommitsByDay(it.time.toInt(), it.commits) }.toList(),

                averageCommitsByDayPeriods = this.filter { it.type == Distribution.Type.PERIOD }
                        .map { AverageCommitsByDayPeriods(it.time, it.commits) }.toList()
        )