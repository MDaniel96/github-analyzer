package bme.softwarearchitectures.githubanalyzerserver.model

class SingleRepositoryResult(

        val repositoryName: String,

        val contributionResponse: ContributionResponse,
        val modificationResponse: ModificationResponse,
        val distributionResponse: DistributionResponse
)