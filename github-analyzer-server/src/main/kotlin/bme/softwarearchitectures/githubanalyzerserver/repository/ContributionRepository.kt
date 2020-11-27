package bme.softwarearchitectures.githubanalyzerserver.repository

import bme.softwarearchitectures.githubanalyzerserver.model.Contribution
import org.springframework.data.jpa.repository.JpaRepository

interface ContributionRepository : JpaRepository<Contribution, Long> {

    fun findByRepositoryUrl(url: String): List<Contribution>
}