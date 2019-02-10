package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.User
import kg.enesai.toshok.enums.AccountStatus

data class ProfileDto(
        val status: AccountStatus?,
        val fullname: String?,
        val checkNumber: String?,
        val level: Int?,
        val giftsGiven: Int?,
        val parentName: String?,
        val phoneNumber: String?,
        val username: String,
        val roleName: String
) {
    companion object {
        fun of(user: User) = ProfileDto(
                user.account?.status,
                user.account?.fullname,
                user.account?.checkNumber,
                user.account?.level,
                user.account?.gifts?.size,
                user.account?.parent?.fullname,
                user.account?.phoneNumber,
                user.username,
                user.role.name
        )
    }
}