package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.model.compare.DeveloperCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.model.compare.DevelopmentCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.model.compare.DoubleRepositoryRequest

interface DoubleRepositoryService {

    fun analyze(request: DoubleRepositoryRequest)

    fun getDevelopmentCompareResult(request: DoubleRepositoryRequest): DevelopmentCompareResponse?

    fun getDeveloperCompareResult(request: DoubleRepositoryRequest): DeveloperCompareResponse?
}