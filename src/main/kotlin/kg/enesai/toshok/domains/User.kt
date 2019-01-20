package kg.enesai.toshok.domains

import javax.persistence.*

@Entity
@Table(name = "users")
class User(
        @Column(name = "username", unique = true)
        val username: String,

        @Column(name = "password")
        var password: String,

        @ManyToOne
        @JoinColumn(name = "role_id")
        val role: Role
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
}