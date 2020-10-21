package bme.softwarearchitectures.githubanalyzerserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GithubAnalyzerServerApplication

fun main(args: Array<String>) {
	runApplication<GithubAnalyzerServerApplication>(*args)
}
