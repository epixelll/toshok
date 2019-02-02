package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.dtos.UpdateAccountDto
import kg.enesai.toshok.enums.AccountStatus
import kg.enesai.toshok.repositories.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException

@Service
class DefaultAccountService(
        private val accountRepository: AccountRepository,
        private val regionService: RegionService
) : AccountService {
    @Transactional(readOnly = true)
    override fun findAll(): List<Account> {
        return accountRepository.findAll()
    }

    @Transactional(readOnly = true)
    override fun get(id: Int): Account {
        return accountRepository.getOne(id)
    }

    @Transactional
    override fun create(account: Account): Account{
        account.parent?.id?.takeIf { accountRepository.countByParentIdAndStatus(it, AccountStatus.APPROVED) >= 4}
                ?.let { throw IllegalStateException("Parent Account already has 4 active children") }
        return accountRepository.save(account)
    }

    @Transactional
    override fun update(id: Int, dto: UpdateAccountDto): Account {
        val account = dtoToAccount(id, dto)
        return accountRepository.save(account)
    }

    @Transactional
    override fun delete(id: Int) {
        accountRepository.deleteById(id)
    }

    private fun dtoToAccount(id: Int, dto: UpdateAccountDto): Account {
        val account = get(id)
        account.fullname = dto.fullname
        account.checkNumber = dto.checkNumber
        account.region = dto.regionId?.let { regionService.get(it) }
        account.parent = dto.parentId?.let { get(it) }
        dto.checkNumber?.let { account.status = AccountStatus.PENDING }

        return account
    }
}