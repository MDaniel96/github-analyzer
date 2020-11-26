package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.dto.ContributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.DistributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.ModificationResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DeveloperCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.compare.DevelopmentCompareResponse
import org.kohsuke.github.GHCommit

interface RepositoryAnalyzer {

    fun generateContributionResponse(commits: Array<GHCommit>): ContributionResponse

    fun generateModificationResponse(commits: Array<GHCommit>): ModificationResponse

    fun generateDistributionResponse(commits: Array<GHCommit>): DistributionResponse

    fun generateDevelopmentCompareResponse(commits1: Array<GHCommit>, commits2: Array<GHCommit>): DevelopmentCompareResponse

    fun generateDeveloperCompareResponse(commits1: Array<GHCommit>, commits2: Array<GHCommit>): DeveloperCompareResponse
}