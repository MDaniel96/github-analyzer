package bme.softwarearchitectures.githubanalyzerserver.repository

import bme.softwarearchitectures.githubanalyzerserver.model.Distribution
import org.springframework.data.jpa.repository.JpaRepository

interface DistributionRepository : JpaRepository<Distribution, Long> {

    fun findByRepositoryUrl(url: String): List<Distribution>
}