package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.enums.AccountStatus
import java.time.LocalDate

data class AccountDto(
        val id: Int,
        val status: AccountStatus,
        val fullname: String,
        val checkNumber: String?,
        val checkPath: String?,
        val level: Int,
        val giftsGiven: Int,
        val parentId: Int?,
        val parentName: String?,
        val parentStatus: AccountStatus?,
        val approvedSiblingCount: Int,
        val phoneNumber: String?,
        val registeredDate: LocalDate?
) {
    companion object {
        fun of(account: Account) = AccountDto(
                account.id!!,
                account.status,
                account.fullname,
                account.checkNumber,
                account.checkPath,
                account.level,
                account.gifts.size,
                account.parent?.id,
                account.parent?.fullname,
                account.parent?.status,
                account.parent?.children?.filter { it.status == AccountStatus.APPROVED }?.size ?: 0,
                account.phoneNumber,
                account.registeredDate
        )
    }
}