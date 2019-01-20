package kg.enesai.toshok.domains

import javax.persistence.*

@Entity
@Table(name = "regions")
class Region(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,

        @Column(name = "name")
        val name: String
)