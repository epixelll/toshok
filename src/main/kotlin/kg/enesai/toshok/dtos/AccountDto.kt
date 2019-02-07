package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.enums.AccountStatus

data class AccountDto(
        val id: Int,
        val status: AccountStatus,
        val fullname: String,
        val checkNumber: String?,
        val level: Int,
        val giftsGiven: Int,
        val parentName: String?,
        val phoneNumber: String?
) {
    companion object {
        fun of(account: Account) = AccountDto(
                account.id!!,
                account.status,
                account.fullname,
                account.checkNumber,
                account.level,
                account.giftGivenForLevel,
                account.parent?.fullname,
                account.phoneNumber
        )
    }
}