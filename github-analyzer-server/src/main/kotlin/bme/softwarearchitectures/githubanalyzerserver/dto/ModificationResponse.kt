package bme.softwarearchitectures.githubanalyzerserver.dto

class ModificationResponse(
        val modificationsByDate: List<ModificationsByDate>
)

class ModificationsByDate(
        val year: Int,
        val month: Int,
        val addedLines: Int,
        val removedLines: Int
)
