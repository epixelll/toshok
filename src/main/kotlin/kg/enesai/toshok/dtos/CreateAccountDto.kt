package kg.enesai.toshok.dtos

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.NotBlank

class CreateAccountDto {
    @NotBlank
    var fullname: String = ""

    @NotBlank
    var address: String = ""


    var checkNumber: String? = null

    @NotBlank
    var passportNumber: String? = null

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    var registeredDate: LocalDate? = null

    var regionId: Int? = null

    var parentId: Int? = null

    @NotBlank
    var phoneNumber: String = ""

    @NotBlank
    var password: String = ""
}