package kg.enesai.toshok.domains

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "gifts")
class Gift(
        @Column(name = "name")
        val name: String,

        @Column(name = "description")
        val description: String?,

        @Column(name = "given_date")
        val givenDate: LocalDate,

        @ManyToOne
        @JoinColumn(name = "account_id")
        val account: Account
) {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int? = null
}