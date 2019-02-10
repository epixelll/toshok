package kg.enesai.toshok.dtos

import kg.enesai.toshok.enums.AccountStatus

data class ChildAccountDto(
        val id: Int,
        val fullname: String,
        val status: AccountStatus?,
        val phoneNumber: String?,
        val giftsGiven: Int,
        val children2: List<SubchildAccountDto>?,
        val children3: List<SubchildAccountDto>?,
        val children4: List<SubchildAccountDto>?
)