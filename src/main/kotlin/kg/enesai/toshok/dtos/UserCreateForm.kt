package kg.enesai.toshok.dtos

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class UserCreateForm(
        @field:NotBlank
        var username: String?,

        @field:NotBlank
        var password: String?,

        var roleId: Int?,

        var accountId: Int?
)