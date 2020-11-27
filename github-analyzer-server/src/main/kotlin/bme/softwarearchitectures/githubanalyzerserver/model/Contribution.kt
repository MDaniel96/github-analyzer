package bme.softwarearchitectures.githubanalyzerserver.model

import javax.persistence.*

@Entity
@Table(name = "analyzer_contribution")
class Contribution(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long = -1,

        @Column(unique = true, nullable = false)
        var developerName: String = "",

        @Column(nullable = false)
        var commits: Int = 0,

        @Column(nullable = false)
        var totalCommits: Int = 0,

        @Column(nullable = false)
        val repositoryUrl: String
)