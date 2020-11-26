package bme.softwarearchitectures.githubanalyzerserver.dto.compare

class DevelopmentCompareResponse(

        val repository1Developers: Int,
        val repository2Developers: Int,

        val repository1CommitsByMonth: List<CommitsByMonth>,
        val repository2CommitsByMonth: List<CommitsByMonth>
)