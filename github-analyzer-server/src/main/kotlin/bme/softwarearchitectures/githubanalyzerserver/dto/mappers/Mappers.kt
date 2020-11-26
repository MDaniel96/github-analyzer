package bme.softwarearchitectures.githubanalyzerserver.dto.mappers

import bme.softwarearchitectures.githubanalyzerserver.model.Commit
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
