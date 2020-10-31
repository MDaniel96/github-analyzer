package bme.softwarearchitectures.githubanalyzerserver.model

class ContributionResponse(

        val totalCommits: Int,
        val commitsByDevelopers: List<CommitsByDeveloper>
)