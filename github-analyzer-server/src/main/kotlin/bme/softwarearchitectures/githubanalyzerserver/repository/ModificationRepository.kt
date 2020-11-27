package bme.softwarearchitectures.githubanalyzerserver.repository

import bme.softwarearchitectures.githubanalyzerserver.model.Modification
import org.springframework.data.jpa.repository.JpaRepository

interface ModificationRepository : JpaRepository<Modification, Long> {

    fun findByRepositoryUrl(url: String): List<Modification>
}