package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.Gift
import java.time.LocalDate

data class GiftDto(
        val id: Int,
        val name: String,
        val description: String?,
        val givenDate: LocalDate
) {
    companion object {
        fun of(gift: Gift) = GiftDto(
                gift.id!!,
                gift.name,
                gift.description,
                gift.givenDate
        )
    }
}