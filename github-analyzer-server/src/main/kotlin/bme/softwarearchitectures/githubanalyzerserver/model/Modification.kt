package bme.softwarearchitectures.githubanalyzerserver.model

import javax.persistence.*

@Entity
@Table(name = "analyzer_modification")
class Modification(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long = -1,

        @Column(nullable = false)
        var year: Int = 0,

        @Column(nullable = false)
        var month: Int = 0,

        @Column(nullable = false)
        var addedLines: Int = 0,

        @Column(nullable = false)
        var removedLines: Int = 0,

        @Column(nullable = false)
        val repositoryUrl: String
)