package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.validators.FourApprovedChilds
import kg.enesai.toshok.validators.UniqueFullname
import kg.enesai.toshok.validators.UniqueFullnameOnUpdate
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@UniqueFullnameOnUpdate
data class AccountUpdateForm(
        @field:NotNull
        var id: Int,

        @field:NotBlank
        var fullname: String?,

        var address: String?,

        var checkNumber: String?,

        var checkPhoto: MultipartFile?,

//        val checkPath: String?,

        var passportNumber: String?,

        @field:DateTimeFormat(pattern = "dd.MM.yyyy")
        var registeredDate: LocalDate?,

        var regionId: Int?,

        @field:FourApprovedChilds
        var parentId: Int?,

        var phoneNumber: String?
) {
        companion object {
            fun of(account: Account) = AccountUpdateForm(
                    account.id!!,
                    account.fullname,
                    account.address,
                    account.checkNumber,
                    null,
//                    account.checkPath,
                    account.passportNumber,
                    account.registeredDate,
                    account.region?.id,
                    account.parent?.id,
                    account.phoneNumber
            )
        }
}