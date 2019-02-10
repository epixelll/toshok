package kg.enesai.toshok.dtos

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class GiftCreateForm(
        @field:NotBlank
        var name: String?,

        @field:NotBlank
        var description: String?,

        @field:NotNull
        var accountId: Int?,

        @field:DateTimeFormat(pattern = "dd.MM.yyyy")
        var givenDate: LocalDate?
)