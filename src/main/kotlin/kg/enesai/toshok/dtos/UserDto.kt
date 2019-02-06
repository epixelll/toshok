package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.User

data class UserDto(
        val id: Int,
        val username: String,
        val roleName: String,
        val accountFullname: String?
) {
    companion object {
        fun of(user: User) = UserDto(
                user.id!!,
                user.username,
                user.role.name,
                user.account?.fullname
        )
    }
}