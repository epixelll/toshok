package kg.enesai.toshok.dtos

import kg.enesai.toshok.validators.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ParentNotNull
data class RegisterForm(
        @field:NotBlank
        @field:UniqueFullname
        var fullname: String?,

        @field:NotBlank
        var address: String?,

        var checkNumber: String?,

        @MultipartNotNull
        var checkPhoto: MultipartFile?,

        @field:NotBlank
        var passportNumber: String?,

        @field:NotNull
        var regionId: Int?,

        @NotNull
        var topOfHierarchy: Boolean = false,

        @field:FourApprovedChilds
        var parentId: Int?,

        @field:NotBlank
        @field:Size(min = 9, max = 13)
        @UniqueUsername
        var phoneNumber: String?,

        @field:NotBlank
        var password: String?
)