package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.dto.ContributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.DistributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.ModificationResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DeveloperCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DevelopmentCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.model.Commit

interface RepositoryAnalyzer {

    fun generateContributionResponse(commits: Collection<Commit>): ContributionResponse

    fun generateModificationResponse(commits: Collection<Commit>): ModificationResponse

    fun generateDistributionResponse(commits: Collection<Commit>): DistributionResponse

    fun generateDevelopmentCompareResponse(commits1: Collection<Commit>, commits2: Collection<Commit>): DevelopmentCompareResponse

    fun generateDeveloperCompareResponse(commits1: Collection<Commit>, commits2: Collection<Commit>): DeveloperCompareResponse
}