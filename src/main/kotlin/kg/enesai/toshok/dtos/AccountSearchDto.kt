package kg.enesai.toshok.dtos

import kg.enesai.toshok.enums.AccountStatus
import javax.validation.constraints.NotNull

data class AccountSearchDto(
        @field:NotNull
        var fullname: String? = "",
        var status: AccountStatus?,
        var level: Int?
)