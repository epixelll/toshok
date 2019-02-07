package kg.enesai.toshok.domains

import kg.enesai.toshok.enums.AccountStatus
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "accounts")
class Account(
        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        var status: AccountStatus,

        @Column(name = "fullname", nullable = false)
        var fullname: String,

        @Column(name = "address")
        var address: String?,

        @Column(name = "check_number")
        var checkNumber: String?,

        @Column(name = "passport_number")
        var passportNumber: String?,

        @Column(name = "phone_number")
        var phoneNumber: String?,

        @Column(name = "registered_date")
        var registeredDate: LocalDate?,

        @ManyToOne
        @JoinColumn(name = "region_id")
        var region: Region?,

        @ManyToOne
        @JoinColumn(name = "parent_id")
        var parent: Account?,

        @Column(name = "level")
        var level: Int,

        @Column(name = "gift_given_for_level")
        var giftGivenForLevel: Int

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null


    @OneToMany(mappedBy = "parent")
    var children: List<Account> = mutableListOf()
}