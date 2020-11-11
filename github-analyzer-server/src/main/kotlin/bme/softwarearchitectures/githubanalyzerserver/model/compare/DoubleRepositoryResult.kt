package bme.softwarearchitectures.githubanalyzerserver.model.compare

class DoubleRepositoryResult(

        val repository1Name: String,
        val repository2Name: String,

        val developmentCompareResponse: DevelopmentCompareResponse,
        val developerCompareResponse: DeveloperCompareResponse
)