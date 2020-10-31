package bme.softwarearchitectures.githubanalyzerserver.model

class SingleRepositoryRequest(
        val repositoryUrl: String,
        val accessToken: String?
) {

    val repositoryId: String
        get() {
            val urlInfo = repositoryUrl.split("/")
            return "${urlInfo[3]}/${urlInfo[4]}"
        }
}