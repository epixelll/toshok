package kg.enesai.toshok.dtos

import javax.validation.constraints.NotBlank

data class ChangePasswordForm(
        @field:NotBlank
        var oldPassword: String?,

        @field:NotBlank
        var newPassword: String?
)