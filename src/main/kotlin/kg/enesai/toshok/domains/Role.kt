package kg.enesai.toshok.domains

import kg.enesai.toshok.enums.Permission
import javax.persistence.*

@Entity
@Table(name = "roles")
class Role(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,

        @Column(name = "name")
        val name: String,

        @ElementCollection(targetClass = Permission::class)
        @Enumerated(EnumType.STRING)
        @CollectionTable(name = "roles_permissions")
        @Column(name = "permission")
        val permissions: Set<Permission>
)