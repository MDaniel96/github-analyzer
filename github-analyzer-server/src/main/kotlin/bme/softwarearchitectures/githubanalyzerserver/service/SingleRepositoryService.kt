package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.model.ContributionResponse
import bme.softwarearchitectures.githubanalyzerserver.model.DistributionResponse
import bme.softwarearchitectures.githubanalyzerserver.model.ModificationResponse
import bme.softwarearchitectures.githubanalyzerserver.model.SingleRepositoryRequest

interface SingleRepositoryService {

    fun analyze(request: SingleRepositoryRequest)

    fun getContributionResultMap(request: SingleRepositoryRequest): ContributionResponse?

    fun getModificationResultMap(request: SingleRepositoryRequest): ModificationResponse?

    fun getDistributionResultMap(request: SingleRepositoryRequest): DistributionResponse?
}