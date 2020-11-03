package bme.softwarearchitectures.githubanalyzerserver.model

class DistributionResponse(
        val averageCommitsByMonth: List<AverageCommitsByMonth>,
        val averageCommitsByDay: List<AverageCommitsByDay>,
        val averageCommitsByDayPeriods: List<AverageCommitsByDayPeriods>
)

class AverageCommitsByMonth(
        val month: Int,
        val commits: Double
)

class AverageCommitsByDay(
        val day: Int,
        val commits: Double
)

class AverageCommitsByDayPeriods(
        val period: String,
        val commits: Double
)
