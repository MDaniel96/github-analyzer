package bme.softwarearchitectures.githubanalyzerserver.model.compare

data class DoubleRepositoryRequest(
        val repository1Url: String,
        val repository2Url: String,
        val accessToken: String?
) {

    val repository1Id: String
        get() {
            val urlInfo = repository1Url.split("/")
            return "${urlInfo[3]}/${urlInfo[4]}"
        }

    val repository2Id: String
        get() {
            val urlInfo = repository2Url.split("/")
            return "${urlInfo[3]}/${urlInfo[4]}"
        }
}