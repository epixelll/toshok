package kg.enesai.toshok.domains

import javax.persistence.*

@Entity
@Table(name = "users")
class User(
        @Column(name = "username", unique = true, nullable = false, updatable = false)
        val username: String,

        @Column(name = "password", nullable = false)
        var password: String,

        @ManyToOne
        @JoinColumn(name = "role_id", nullable = false)
        val role: Role
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null
}