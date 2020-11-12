package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.model.SingleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.model.SingleRepositoryResult

interface SingleRepositoryService {

    fun analyze(request: SingleRepositoryRequest)

    fun getRepositoryInfo(request: SingleRepositoryRequest): SingleRepositoryResult?
}