package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.model.SingleRepositoryRequest
import bme.softwarearchitectures.githubanalyzerserver.model.SingleRepositoryResult
import org.kohsuke.github.GitHub
import org.springframework.stereotype.Service

@Service
class SingleRepositoryServiceImpl : SingleRepositoryService {

    private val github = GitHub.connectAnonymously()

    override fun analyze(request: SingleRepositoryRequest) {
        // TODO: analyze repository and save it to memory db
        // TODO: analyze private repository
    }

    override fun getRepositoryInfo(repositoryUrl: String, accessToken: String): SingleRepositoryResult? {
        // TODO: return analyzed repository from memory db or null if not found
        return null
    }
}