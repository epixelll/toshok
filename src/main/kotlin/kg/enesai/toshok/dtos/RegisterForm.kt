package kg.enesai.toshok.dtos

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class RegisterForm(
        @field:NotBlank
        var fullname: String?,

        @field:NotBlank
        var address: String?,

        var checkNumber: String?,

        @field:NotBlank
        var passportNumber: String?,

        @DateTimeFormat(pattern = "dd.MM.yyyy")
        var registeredDate: LocalDate?,

        @field:NotNull
        var regionId: Int?,

        var parentId: Int?,

        @field:NotBlank
        @field:Size(min = 9, max = 13)
        var phoneNumber: String?,

        @field:NotBlank
        var password: String?
)