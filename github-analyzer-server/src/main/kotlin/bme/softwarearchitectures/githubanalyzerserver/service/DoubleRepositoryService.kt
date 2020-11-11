package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.model.compare.DoubleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.model.compare.DoubleRepositoryResult

interface DoubleRepositoryService {

    fun analyze(request: DoubleRepositoryRequest)

    fun getRepositoryInfo(repository1Url: String, repository2Url: String, accessToken: String = ""): DoubleRepositoryResult?
}