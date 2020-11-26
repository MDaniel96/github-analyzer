package bme.softwarearchitectures.githubanalyzerserver.dto.compare

class CommitsByMonth(
        val year: Int,
        val month: Int,
        val commits: Int
)