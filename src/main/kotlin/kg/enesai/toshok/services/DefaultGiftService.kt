package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Gift
import kg.enesai.toshok.dtos.GiftCreateForm
import kg.enesai.toshok.dtos.GiftDto
import kg.enesai.toshok.repositories.GiftRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class DefaultGiftService(
        private val giftRepository: GiftRepository,
        private val accountService: AccountService
): GiftService {
    @Transactional(readOnly = true)
    override fun findAllByAccountId(accountId: Int): List<GiftDto> {
        return giftRepository.findAllByAccountId(accountId).map { GiftDto.of(it) }
    }

    @Transactional
    override fun create(giftCreateForm: GiftCreateForm){
        val gift = formToEntity(giftCreateForm)
        giftRepository.save(gift)
    }

    private fun formToEntity(giftCreateForm: GiftCreateForm): Gift {
        return Gift(
                giftCreateForm.name!!,
                giftCreateForm.description,
                giftCreateForm.givenDate ?: LocalDate.now(),
                accountService.findById(giftCreateForm.accountId!!)

        )
    }

}