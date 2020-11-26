package bme.softwarearchitectures.githubanalyzerserver.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "analyzer_commit")
class Commit(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long = -1,

        @Column(nullable = false)
        var author: String = "",

        @Column(nullable = false)
        var created: LocalDateTime = LocalDateTime.now(),

        @Column(nullable = false)
        var linesAdded: Int = 0,

        @Column(nullable = false)
        var linesDeleted: Int = 0,

        @ManyToOne
        val repository: Repository
)