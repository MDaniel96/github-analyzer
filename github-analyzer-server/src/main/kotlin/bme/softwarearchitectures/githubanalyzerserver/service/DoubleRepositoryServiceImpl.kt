package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.config.AppConfig
import bme.softwarearchitectures.githubanalyzerserver.model.compare.CommitsByMonth
import bme.softwarearchitectures.githubanalyzerserver.model.compare.DeveloperCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.model.compare.DevelopmentCompareResponse
import bme.softwarearchitectures.githubanalyzerserver.model.compare.DoubleRepositoryRequest
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GitHub
import org.springframework.stereotype.Service

@Service
class DoubleRepositoryServiceImpl(val appConfig: AppConfig) : DoubleRepositoryService {

    val developmentCompareResultMap = mutableMapOf<DoubleRepositoryRequest, DevelopmentCompareResponse>()
    val developerCompareResultMap = mutableMapOf<DoubleRepositoryRequest, DeveloperCompareResponse>()

    override fun analyze(request: DoubleRepositoryRequest) {
        val gitHubAPI1 = GitHub.connectUsingOAuth(
                if (request.access1Token != "")
                    request.access1Token
                else
                    appConfig.accessToken
        )

        val gitHubAPI2 = GitHub.connectUsingOAuth(
                if (request.access2Token != "")
                    request.access2Token
                else
                    appConfig.accessToken
        )

        val ghRepository1 = gitHubAPI1.getRepository(request.repository1Id)
        val ghRepository2 = gitHubAPI2.getRepository(request.repository2Id)

        if (ghRepository1 !== null && ghRepository2 !== null) {
            val commits1 = ghRepository1.listCommits().toArray()
            val commits2 = ghRepository2.listCommits().toArray()

            developmentCompareResultMap[request] = generateDevelopmentCompareResponse(commits1, commits2)
            developerCompareResultMap[request] = generateDeveloperCompareResponse(commits1, commits2)
        }
    }

    override fun getDevelopmentCompareResult(request: DoubleRepositoryRequest) = developmentCompareResultMap[request]

    override fun getDeveloperCompareResult(request: DoubleRepositoryRequest) = developerCompareResultMap[request]

    private fun generateDevelopmentCompareResponse(commits1: Array<GHCommit>, commits2: Array<GHCommit>): DevelopmentCompareResponse {
        val repository1Developers = commits1.groupBy { it.commitShortInfo.author.name }.count()
        val repository2Developers = commits2.groupBy { it.commitShortInfo.author.name }.count()

        val repository1CommitsByMonth = getCommitsByMonthOfLastYear(commits1).fillEmpty().sortedBy { it.month }
        val repository2CommitsByMonth = getCommitsByMonthOfLastYear(commits2).fillEmpty().sortedBy { it.month }

        return DevelopmentCompareResponse(repository1Developers, repository2Developers, repository1CommitsByMonth, repository2CommitsByMonth)
    }

    private fun generateDeveloperCompareResponse(commits1: Array<GHCommit>, commits2: Array<GHCommit>): DeveloperCompareResponse {
        val developer1Name = commits1.groupBy { it.commitShortInfo.author.name }.maxBy { (_, commits) -> commits.count() }!!.key
        val developer2Name = commits2.groupBy { it.commitShortInfo.author.name }.maxBy { (_, commits) -> commits.count() }!!.key

        val developer1CommitsByMonth = getCommitsByMonthOfLastYear(commits1, developer1Name).fillEmpty().sortedBy { it.month }
        val developer2CommitsByMonth = getCommitsByMonthOfLastYear(commits2, developer2Name).fillEmpty().sortedBy { it.month }

        return DeveloperCompareResponse(developer1Name, developer2Name, developer1CommitsByMonth, developer2CommitsByMonth)
    }

    private fun getCommitsByMonthOfLastYear(commits: Array<GHCommit>, developerName: String = ""): MutableList<CommitsByMonth> {
        val lastYear = 1900 + commits.maxBy { it.commitShortInfo.commitDate.year }!!.commitShortInfo.commitDate.year

        val commitsByMonth = mutableListOf<CommitsByMonth>()
        commits.groupBy { "${it.commitShortInfo.commitDate.year}|${it.commitShortInfo.commitDate.month}" }
                .forEach { (date, commits) ->
                    val splitDate = date.split("|")
                    val year = 1900 + splitDate[0].toInt()
                    if (year == lastYear && developerName == "") {
                        commitsByMonth.add(CommitsByMonth(year, splitDate[1].toInt(), commits.count()))
                    } else if (year == lastYear && developerName != "" && commits.isNotEmpty()) {
                        val count = commits.filter { it.commitShortInfo.author.name == developerName }.count()
                        commitsByMonth.add(CommitsByMonth(year, splitDate[1].toInt(), count))
                    }
                }
        return commitsByMonth
    }

    fun MutableList<CommitsByMonth>.fillEmpty(): MutableList<CommitsByMonth> {
        val year = this[0].year
        val filledMonths = this.map { it.month }.toSet()
        for (month in 0..11) {
            if (!filledMonths.contains(month)) {
                this.add(CommitsByMonth(year = year, month = month, commits = 0))
            }
        }
        return this
    }
}
