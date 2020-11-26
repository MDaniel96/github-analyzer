package bme.softwarearchitectures.githubanalyzerserver.dto

class ContributionResponse(

        val totalCommits: Int,
        val commitsByDevelopers: List<CommitsByDeveloper>
)

class CommitsByDeveloper(
        val developerName: String,
        val commits: Int
)