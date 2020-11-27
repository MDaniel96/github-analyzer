package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.dto.mappers.toCommitList
import bme.softwarearchitectures.githubanalyzerserver.model.Commit
import bme.softwarearchitectures.githubanalyzerserver.model.Repository
import bme.softwarearchitectures.githubanalyzerserver.repository.CommitRepository
import bme.softwarearchitectures.githubanalyzerserver.repository.RepositoryRepository
import org.kohsuke.github.GHCommit
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CommitServiceImpl(
        val repositoryRepository: RepositoryRepository,
        val commitRepository: CommitRepository
) : CommitService {

    override fun saveRepositoryWithCommits(repositoryUrl: String, repositoryName: String, commits: Array<GHCommit>): Collection<Commit> {
        val repositoryToSave = repositoryRepository.save(
                Repository(
                        url = repositoryUrl,
                        name = repositoryName,
                        lastQueried = LocalDateTime.now()
                )
        )
        val commitsToSave = commits.toCommitList(repositoryToSave)
        commitRepository.saveAll(commitsToSave)
        repositoryToSave.commits = commitsToSave
        return commitsToSave
    }

    override fun saveCommits(newCommits: Array<GHCommit>, repository: Repository) {
        val commitsToSave = newCommits.toCommitList(repository)
        repository.commits.addAll(commitsToSave)
        commitRepository.saveAll(commitsToSave)
    }
}