package kg.enesai.toshok.dtos

import javax.validation.constraints.NotBlank

data class UpdateAccountDto(
        @NotBlank
        val fullname: String,

        val checkNumber: String?,

        val regionId: Int?,

        val parentId: Int?
)