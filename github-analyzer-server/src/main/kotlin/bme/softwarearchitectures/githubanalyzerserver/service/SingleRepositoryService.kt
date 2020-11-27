package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.dto.ContributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.DistributionResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.ModificationResponse
import bme.softwarearchitectures.githubanalyzerserver.dto.SingleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.model.Commit
import bme.softwarearchitectures.githubanalyzerserver.model.Repository
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GitHub

interface SingleRepositoryService {

    fun analyze(request: SingleRepositoryRequest)

    fun getContributionResultMap(request: SingleRepositoryRequest): ContributionResponse?

    fun getModificationResultMap(request: SingleRepositoryRequest): ModificationResponse?

    fun getDistributionResultMap(request: SingleRepositoryRequest): DistributionResponse?

    fun connectGithub(accessToken: String?): GitHub

    fun saveCommits(newCommits: Array<GHCommit>, repository: Repository)

    fun saveRepositoryWithCommits(repositoryUrl: String, repositoryName: String, commits: Array<GHCommit>): Collection<Commit>
}