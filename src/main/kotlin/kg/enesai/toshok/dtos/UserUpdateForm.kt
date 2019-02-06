package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.User
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UserUpdateForm(
        @field:NotNull
        var id: Int,

        @field:NotBlank
        var username: String?,

        @field:NotBlank
        var password: String?,

        @field:NotNull
        var roleId: Int?,

        var accountId: Int?
) {
        companion object {
            fun of(user: User) = UserUpdateForm(
                    user.id!!,
                    user.username,
                    null,
                    user.role.id,
                    user.account?.id
            )
        }
}