package kg.enesai.toshok.dtos

import kg.enesai.toshok.enums.AccountStatus

data class AccountInfo(
        val id: Int,
        val status: AccountStatus,
        val fullname: String,
        val address: String?,
        val checkNumber: String?,
        val checkPath: String?,
        val level: Int,
        val phoneNumber: String?,
        val parentName: String?,
        val parentId: Int?,
        val children: List<ChildAccountDto>?
)