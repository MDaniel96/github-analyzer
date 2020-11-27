package bme.softwarearchitectures.githubanalyzerserver.dto.mappers

import bme.softwarearchitectures.githubanalyzerserver.dto.CommitsByDeveloper
import bme.softwarearchitectures.githubanalyzerserver.dto.ContributionResponse
import bme.softwarearchitectures.githubanalyzerserver.model.Commit
import bme.softwarearchitectures.githubanalyzerserver.model.Contribution
import bme.softwarearchitectures.githubanalyzerserver.model.Repository
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