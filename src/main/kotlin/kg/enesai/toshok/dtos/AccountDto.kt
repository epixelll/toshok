package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.enums.AccountStatus

data class AccountDto(
        val id: Int,
        val status: AccountStatus,
        val fullname: String,
        val checkNumber: String?,
        val level: Int,
        val parentName: String?,
        val phoneNumber: String?
) {
    companion object {
        fun of(account: Account, level: Int) = AccountDto(
                account.id!!,
                account.status,
                account.fullname,
                account.checkNumber,
                level,
                account.parent?.fullname,
                account.phoneNumber
        )
    }
}