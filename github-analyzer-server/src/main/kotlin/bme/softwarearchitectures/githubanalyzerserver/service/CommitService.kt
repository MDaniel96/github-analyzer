package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.model.Commit
import bme.softwarearchitectures.githubanalyzerserver.model.Repository
import org.kohsuke.github.GHCommit

interface CommitService {

    fun saveCommits(newCommits: Array<GHCommit>, repository: Repository)

    fun saveRepositoryWithCommits(repositoryUrl: String, repositoryName: String, commits: Array<GHCommit>): Collection<Commit>
}