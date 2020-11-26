package bme.softwarearchitectures.githubanalyzerserver.repository

import bme.softwarearchitectures.githubanalyzerserver.model.Commit
import org.springframework.data.jpa.repository.JpaRepository

interface CommitRepository : JpaRepository<Commit, Long> {
}