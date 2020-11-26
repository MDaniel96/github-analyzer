package bme.softwarearchitectures.githubanalyzerserver.dto.compare

class DeveloperCompareResponse(

        val developer1Name: String,
        val developer2Name: String,

        val developer1CommitsByMonth: List<CommitsByMonth>,
        val developer2CommitsByMonth: List<CommitsByMonth>
)