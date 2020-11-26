package bme.softwarearchitectures.githubanalyzerserver.repository

import bme.softwarearchitectures.githubanalyzerserver.model.Repository
import org.springframework.data.jpa.repository.JpaRepository

interface RepositoryRepository : JpaRepository<Repository, Long> {

    fun findByUrl(url: String): Repository?
}