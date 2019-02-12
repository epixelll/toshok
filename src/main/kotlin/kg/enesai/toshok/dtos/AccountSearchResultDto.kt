package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.Account

data class AccountSearchResultDto(
        val id: Int,
        val fullname: String
) {
    companion object {
        fun of(account: Account) = AccountSearchResultDto(
                account.id!!,
                account.fullname
        )
    }
}