package bme.softwarearchitectures.githubanalyzerserver.model

class ModificationsByDate(
        val year: Int,
        val month: Int,
        val addedLines: Int,
        val removedLines: Int
)
