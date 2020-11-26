package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.dto.ContributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.DistributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.ModificationResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.SingleRepositoryRequest

interface SingleRepositoryService {

    fun analyze(request: SingleRepositoryRequest)

    fun getContributionResultMap(request: SingleRepositoryRequest): ContributionResponse?

    fun getModificationResultMap(request: SingleRepositoryRequest): ModificationResponse?

    fun getDistributionResultMap(request: SingleRepositoryRequest): DistributionResponse?
}