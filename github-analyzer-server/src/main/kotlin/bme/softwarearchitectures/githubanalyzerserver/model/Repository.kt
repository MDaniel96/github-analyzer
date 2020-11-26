package bme.softwarearchitectures.githubanalyzerserver.model

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "analyzer_repository")
class Repository(

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id: Long = -1,

        @Column(unique = true, nullable = false)
        var url: String = "",

        @Column(nullable = false)
        var name: String = "",

        @Column(nullable = false)
        var lastQueried: LocalDateTime = LocalDateTime.now(),

        @OneToMany(mappedBy = "repository")
        var commits: MutableSet<Commit> = hashSetOf()
)