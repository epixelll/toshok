package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.dtos.*
import kg.enesai.toshok.enums.AccountStatus
import kg.enesai.toshok.repositories.AccountRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException
import javax.persistence.EntityNotFoundException

@Service
class DefaultAccountService(
        private val accountRepository: AccountRepository,
        private val regionService: RegionService
) : AccountService {
    @Transactional(readOnly = true)
    override fun findAll(): List<AccountDto> {
        return accountRepository.findAll().map { AccountDto.of(it, getLevel(it)) }
    }

    @Transactional(readOnly = true)
    override fun findAll(pageable: Pageable): Page<AccountDto> {
        return accountRepository.findAll(pageable).map { AccountDto.of(it, getLevel(it)) }
    }

    @Transactional(readOnly = true)
    override fun findAllPending(pageable: Pageable): Page<AccountDto> {
        return accountRepository.findAllByStatus(AccountStatus.PENDING, pageable).map { AccountDto.of(it, getLevel(it)) }
    }

    @Transactional(readOnly = true)
    override fun get(id: Int): Account {
        return accountRepository.getOne(id)
    }

    @Transactional(readOnly = true)
    override fun getUpdateForm(id: Int): AccountUpdateForm {
        val account = accountRepository.getOne(id)
        return AccountUpdateForm.of(account)
    }

    @Transactional
    override fun create(account: Account): Account{
        account.parent?.
                takeIf { accountRepository.countByParentIdAndStatus(it.id!!, AccountStatus.APPROVED) >= 4}
                ?.let { throw IllegalStateException("""Parent Account(id = ${it.id}, name = ${it.fullname}) already has 4 active children""") }
        return accountRepository.save(account)
    }

    @Transactional
    override fun create(accountCreateForm: AccountCreateForm): Account{
        val account = formToAccount(accountCreateForm)
        account.parent?.
                takeIf { accountRepository.countByParentIdAndStatus(it.id!!, AccountStatus.APPROVED) >= 4}
                ?.let { throw IllegalStateException("""Parent Account(id = ${it.id}, name = ${it.fullname}) already has 4 active children""") }
        return accountRepository.save(account)
    }

    @Transactional
    override fun createAndFlush(account: Account): Account{
        account.parent?.
                takeIf { accountRepository.countByParentIdAndStatus(it.id!!, AccountStatus.APPROVED) >= 4}
                ?.let { throw IllegalStateException("""Parent Account(id = ${it.id}, name = ${it.fullname}) already has 4 active children""") }
        val savedAccount = accountRepository.save(account)
        accountRepository.flush()
        return savedAccount
    }

    @Transactional
    override fun update(form: AccountUpdateForm): Account {
        val account = formToAccount(form)
        return accountRepository.save(account)
    }

    @Transactional
    override fun delete(id: Int) {
        accountRepository.removeChildsByParentId(id)
        accountRepository.deleteById(id)
    }

    @Transactional
    override fun approve(id: Int) {
        val account = accountRepository.findById(id).orElseThrow { throw EntityNotFoundException("Entity with this id not found") }
        account.status = AccountStatus.APPROVED
        accountRepository.save(account)
    }


    @Transactional(readOnly = true)
    override fun findByFullname(fullname: String?): Account? {
        return accountRepository.findByFullname(fullname)
    }

    @Transactional
    override fun saveAll(list: List<Account>) {
        accountRepository.saveAll(list)
    }

    @Transactional(readOnly = true)
    override fun getLevel(account: Account): Int {
        var level = 1
        if(accountRepository.countByParentIdAndStatus(account.id!!, AccountStatus.APPROVED) == 4) level = 2
        if(accountRepository.countByParentParentIdAndStatus(account.id, AccountStatus.APPROVED) == 16) level = 3
        if(accountRepository.countByParentParentParentIdAndStatus(account.id, AccountStatus.APPROVED) == 64) level = 4
        if(accountRepository.countByParentParentParentParentIdAndStatus(account.id, AccountStatus.APPROVED) == 256) level = 5

        return level
    }

    @Transactional(readOnly = true)
    override fun findById(id: Int) = accountRepository.findById(id).orElseThrow { EntityNotFoundException("Account with id = $id not found") }!!

    @Transactional
    override fun getAccountInfo(id: Int): AccountInfo {
        val account = findById(id)
        return mapToAccountInfo(account)
    }

    private fun mapToAccountInfo(account: Account): AccountInfo {
        val children = account.children.filter { it.status == AccountStatus.APPROVED }.takeIf { it.isNotEmpty() }?.map {child ->
            ChildAccountDto(
                    child.id!!,
                    child.fullname,
                    child.status,
                    child.children.map {SubchildAccountDto(it.id!!, it.fullname, it.status)},
                    child.children.flatMap{it.children.map { SubchildAccountDto(it.id!!, it.fullname, it.status) }},
                    child.children.flatMap{it.children.flatMap { it.children.map { SubchildAccountDto(it.id!!, it.fullname, it.status) }}}
            )
        }
        return AccountInfo(
                account.id!!,
                account.status,
                account.fullname,
                account.checkNumber,
                getLevel(account),
                account.phoneNumber,
                account.parent?.fullname,
                account.parent?.id,
                children
        )
    }

    private fun formToAccount(form: AccountUpdateForm): Account {
        val account = get(form.id)
        account.status = form.checkNumber?.let { AccountStatus.PENDING } ?: AccountStatus.CREATED
        account.fullname = form.fullname!!
        account.checkNumber = form.checkNumber
        account.passportNumber = form.passportNumber
        account.registeredDate = form.registeredDate
        account.region = form.regionId?.let { regionService.get(it) }
        account.parent = form.parentId?.let { get(it) }
        account.phoneNumber = form.phoneNumber

        return account
    }

    private fun formToAccount(createForm: AccountCreateForm): Account {
        return Account(
                createForm.checkNumber?.let { AccountStatus.PENDING } ?: AccountStatus.CREATED,
                createForm.fullname!!,
                createForm.address!!,
                createForm.checkNumber,
                createForm.passportNumber,
                createForm.phoneNumber,
                createForm.registeredDate,
                createForm.regionId?.let { regionService.get(it) },
                createForm.parentId?.let { this.get(it) }
        )
    }
}