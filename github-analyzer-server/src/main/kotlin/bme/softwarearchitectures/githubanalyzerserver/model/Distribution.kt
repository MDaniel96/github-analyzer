package bme.softwarearchitectures.githubanalyzerserver.model

import javax.persistence.*

@Entity
@Table(name = "analyzer_distribution")
class Distribution(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long = -1,

        @Column(nullable = false)
        var type: Type = Type.MONTH,

        @Column(nullable = false)
        var time: String = "",

        @Column(nullable = false)
        var commits: Double = 0.0,

        @Column(nullable = false)
        val repositoryUrl: String
) {
    enum class Type {
        MONTH, DAY, PERIOD
    }
}