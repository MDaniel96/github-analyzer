package bme.softwarearchitectures.githubanalyzerserver.service

import bme.softwarearchitectures.githubanalyzerserver.model.compare.*
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GitHub
import org.springframework.stereotype.Service

@Service
class DoubleRepositoryServiceImpl : DoubleRepositoryService {

    val doubleRepositoryResultMap = mutableMapOf<DoubleRepositoryRequest, DoubleRepositoryResult>()

    private val github = GitHub.connectAnonymously()

    override fun analyze(request: DoubleRepositoryRequest) {
        val githubAPI = request.accessToken?.let {
            GitHub.connectUsingOAuth(request.accessToken)
        } ?: github

        val ghRepository1 = githubAPI.getRepository(request.repository1Id)
        val ghRepository2 = githubAPI.getRepository(request.repository2Id)

        if (ghRepository1 !== null && ghRepository2 !== null) {
            val commits1 = ghRepository1.listCommits().toArray()
            val commits2 = ghRepository2.listCommits().toArray()

            val developmentCompare = generateDevelopmentCompareResponse(commits1, commits2)
            val developerCompare = generateDeveloperCompareResponse(commits1, commits2)

            val result = DoubleRepositoryResult(ghRepository1.name, ghRepository2.name, developmentCompare, developerCompare)
            doubleRepositoryResultMap.put(request, result)
        }
    }

    override fun getRepositoryInfo(request: DoubleRepositoryRequest) = doubleRepositoryResultMap[request]

    private fun generateDevelopmentCompareResponse(commits1: Array<GHCommit>, commits2: Array<GHCommit>): DevelopmentCompareResponse {
        val repository1Developers = commits1.groupBy { it.commitShortInfo.author.name }.count()
        val repository2Developers = commits2.groupBy { it.commitShortInfo.author.name }.count()

        val repository1CommitsByMonth = getCommitsByMonthOfLastYear(commits1)
        val repository2CommitsByMonth = getCommitsByMonthOfLastYear(commits2)

        return DevelopmentCompareResponse(repository1Developers, repository2Developers, repository1CommitsByMonth, repository2CommitsByMonth)
    }

    private fun generateDeveloperCompareResponse(commits1: Array<GHCommit>, commits2: Array<GHCommit>): DeveloperCompareResponse {
        val developer1Name = commits1.groupBy { it.commitShortInfo.author.name }.maxBy { (_, commits) -> commits.count() }!!.key
        val developer2Name = commits2.groupBy { it.commitShortInfo.author.name }.maxBy { (_, commits) -> commits.count() }!!.key

        val developer1CommitsByMonth = getCommitsByMonthOfLastYear(commits1, developer1Name)
        val developer2CommitsByMonth = getCommitsByMonthOfLastYear(commits2, developer2Name)

        return DeveloperCompareResponse(developer1Name, developer2Name, developer1CommitsByMonth, developer2CommitsByMonth)
    }

    private fun getCommitsByMonthOfLastYear(commits: Array<GHCommit>, developerName: String = ""): List<CommitsByMonth> {
        val lastYear = 1900 + commits.maxBy { it.commitShortInfo.commitDate.year }!!.commitShortInfo.commitDate.year

        val commitsByMonth = mutableListOf<CommitsByMonth>()
        commits.groupBy { "${it.commitShortInfo.commitDate.year}|${it.commitShortInfo.commitDate.month}" }
                .forEach { (date, commits) ->
                    val splitDate = date.split("|")
                    val year = 1900 + splitDate[0].toInt()
                    if ((year == lastYear && developerName == "") ||
                            (year == lastYear && developerName != "" && commits.isNotEmpty() &&
                                    developerName == commits[0].commitShortInfo.author.name)) {
                        commitsByMonth.add(CommitsByMonth(year, splitDate[1].toInt(), commits.count()))
                    }
                }
        return commitsByMonth
    }
}