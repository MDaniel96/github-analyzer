package bme.softwarearchitectures.githubanalyzerserver.dto.compare

data class DoubleRepositoryRequest(
        val repository1Url: String,
        val repository2Url: String,
        val access1Token: String?,
        val access2Token: String?
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