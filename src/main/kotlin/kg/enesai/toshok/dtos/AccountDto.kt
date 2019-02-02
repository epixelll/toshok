package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.Account

data class AccountDto(
        val fullname: String,
        val checkNumber: String?,
        val regionId: Int?,
        val parentId: Int?
) {
    companion object {
        fun of(account: Account) = AccountDto(
                account.fullname,
                account.checkNumber,
                account.region?.id,
                account.parent?.id
        )
    }
}