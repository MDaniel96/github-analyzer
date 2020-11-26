package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DeveloperCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DevelopmentCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DoubleRepositoryRequest

interface DoubleRepositoryService {

    fun analyze(request: DoubleRepositoryRequest)

    fun getDevelopmentCompareResult(request: DoubleRepositoryRequest): DevelopmentCompareResponse?

    fun getDeveloperCompareResult(request: DoubleRepositoryRequest): DeveloperCompareResponse?
}