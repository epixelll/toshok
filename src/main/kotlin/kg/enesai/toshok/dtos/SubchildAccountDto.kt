package kg.enesai.toshok.dtos

import kg.enesai.toshok.enums.AccountStatus

data class SubchildAccountDto(
        val id: Int,
        val fullname: String,
        val status: AccountStatus?,
        val phoneNumber: String?,
        val giftsGiven: Int,
        val parentName: String
)