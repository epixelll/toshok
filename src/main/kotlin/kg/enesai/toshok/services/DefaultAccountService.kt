package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.dtos.*
import kg.enesai.toshok.enums.AccountStatus
import kg.enesai.toshok.enums.Permission
import kg.enesai.toshok.repositories.AccountRepository
import kg.enesai.toshok.services.endpoint.FileUploadEndpointService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException
import java.time.LocalDate
import javax.persistence.EntityNotFoundException

@Service
class DefaultAccountService(
        private val accountRepository: AccountRepository,
        private val regionService: RegionService,
        private val fileUploadEndpointService: FileUploadEndpointService,
        private val currentUserService: CurrentUserService
) : AccountService {
    @Transactional(readOnly = true)
    override fun findAll(): List<AccountDto> {
        return accountRepository.findAll().map { AccountDto.of(it) }
    }

    @Transactional(readOnly = true)
    override fun findAll(accountSearchDto: AccountSearchDto, pageable: Pageable): Page<AccountDto> {
        val accounts: Page<Account> = if(accountSearchDto.status != null && accountSearchDto.level != null)
            accountRepository.findAllByFullnameIgnoreCaseContainingAndStatusAndLevel(accountSearchDto.fullname!!, accountSearchDto.status!!, accountSearchDto.level!!, pageable)
        else if(accountSearchDto.status != null) accountRepository.findAllByFullnameIgnoreCaseContainingAndStatus(accountSearchDto.fullname!!, accountSearchDto.status!!, pageable)
        else if(accountSearchDto.level != null) accountRepository.findAllByFullnameIgnoreCaseContainingAndLevel(accountSearchDto.fullname!!, accountSearchDto.level!!, pageable)
        else accountRepository.findAllByFullnameIgnoreCaseContaining(accountSearchDto.fullname!!, pageable)
        return accounts.map { AccountDto.of(it) }
    }

    @Transactional(readOnly = true)
    override fun findAllGiftNeededAccounts(fullname: String, pageable: Pageable): Page<AccountDto> {
        return accountRepository.findAllGiftNeededAccounts(fullname.toLowerCase(), pageable).map { AccountDto.of(it) }
    }

    @Transactional(readOnly = true)
    override fun findAllPending(fullname: String, pageable: Pageable): Page<AccountDto> {
        return accountRepository.findAllByFullnameIgnoreCaseContainingAndStatus(fullname, AccountStatus.PENDING, pageable).map { AccountDto.of(it) }
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
        account.parent?.let{ parent ->
            parent.takeIf { accountRepository.countByParentIdAndStatus(it.id!!, AccountStatus.APPROVED) >= 4}
                    ?.let { throw IllegalStateException("""Parent Account(id = ${it.id}, name = ${it.fullname}) already has 4 active children""") }
            updateParentLevels(parent)
        }

        return accountRepository.save(account)
    }

    @Transactional
    override fun createFromExcel(account: Account): Account{
        System.out.println("****CREATED: " + account.fullname + " id: " + account.id )
        account.parent?.let{ parent ->
            parent.takeIf { accountRepository.countByParentIdAndStatus(it.id!!, AccountStatus.APPROVED) >= 4}
                    ?.let { account.parent = null }
            updateParentLevels(parent)
        }

        return accountRepository.save(account)
    }

    @Transactional
    override fun createTemporalAccount(name: String): Account {
        val account = Account(
                AccountStatus.TEMPORARY,
                name,
                null,
                null,
                null,
                null,
                null,
                LocalDate.now(),
                null,
                null,
                0
        )

        return accountRepository.save(account)
    }

    @Transactional
    override fun create(accountCreateForm: AccountCreateForm): Account{
        val account = formToAccount(accountCreateForm)
        account.parent?.let { parent ->
            parent.takeIf { accountRepository.countByParentIdAndStatus(it.id!!, AccountStatus.APPROVED) >= 4 }
                    ?.let { throw IllegalStateException("""Parent Account(id = ${it.id}, name = ${it.fullname}) already has 4 active children""") }
            updateParentLevels(parent)
        }
        return accountRepository.save(account)
    }

    @Transactional
    override fun createAndFlush(account: Account): Account{
        account.parent?.let {parent ->
            parent.takeIf { accountRepository.countByParentIdAndStatus(it.id!!, AccountStatus.APPROVED) >= 4 }
                    ?.let { throw IllegalStateException("""Parent Account(id = ${it.id}, name = ${it.fullname}) already has 4 active children""") }
            updateParentLevels(parent)
        }
        val savedAccount = accountRepository.save(account)
        accountRepository.flush()
        return savedAccount
    }

    @Transactional
    override fun update(form: AccountUpdateForm): Account {
        var account = get(form.id)
        if(!currentUserService.currentUserHasPermission(Permission.ACCOUNT_UPDATE_APPROVED) && account.status == AccountStatus.APPROVED)
            throw IllegalAccessException("You don't have permission to update Approved accounts")
        account = formToAccount(account, form)
        account.parent?.let { updateParentLevels(it) }
        return accountRepository.save(account)
    }

    @Transactional
    override fun delete(id: Int) {
        val account = accountRepository.findById(id).orElseThrow { EntityNotFoundException("""Account with id = $id not found""") }
        if(!currentUserService.currentUserHasPermission(Permission.ACCOUNT_DELETE_APPROVED) && account.status == AccountStatus.APPROVED)
            throw IllegalAccessException("You don't have permission to delete Approved accounts")
        accountRepository.removeChildsByParentId(id)
        account.parent?.let { updateParentLevels(it) }
        accountRepository.deleteById(id)
    }

    @Transactional
    override fun approve(id: Int) {
        val account = accountRepository.findById(id).orElseThrow { throw EntityNotFoundException("Entity with this id not found") }
        account.status = AccountStatus.APPROVED
        accountRepository.save(account)
        account.parent?.let { updateParentLevels(it) }
    }


    @Transactional(readOnly = true)
    override fun findByFullname(fullname: String?): Account? {
        return accountRepository.findByFullname(fullname).firstOrNull()
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

//    @Transactional
//    override fun giveGift(id: Int) {
//        val account = accountRepository.findById(id).orElseThrow { throw EntityNotFoundException("Entity with this id not found") }
//        account.giftGivenForLevel = account.giftGivenForLevel + 1
//        accountRepository.save(account)
//    }

    @Transactional(readOnly = true)
    override fun findById(id: Int) = accountRepository.findById(id).orElseThrow { EntityNotFoundException("Account with id = $id not found") }!!

    @Transactional
    override fun getAccountInfo(id: Int): AccountInfo {
        val account = findById(id)
        return mapToAccountInfo(account)
    }

    @Transactional
    override fun deleteTemporary() = accountRepository.deleteByStatus(AccountStatus.TEMPORARY)

    private fun updateParentLevels(parent: Account) {
        parent.level = getLevel(parent)
        accountRepository.save(parent)
        parent.parent?.let { updateParentLevels(it) }
    }

    private fun mapToAccountInfo(account: Account): AccountInfo {
        val children = account.children.filter { it.status == AccountStatus.APPROVED }.takeIf { it.isNotEmpty() }?.map {child ->
            ChildAccountDto(
                    child.id!!,
                    child.fullname,
                    child.status,
                    child.phoneNumber,
                    child.gifts.size,
                    child.children.filter { it.status == AccountStatus.APPROVED }
                            .map {SubchildAccountDto(it.id!!, it.fullname, it.status, it.phoneNumber, it.gifts.size, it.parent!!.fullname)},
                    child.children.filter { it.status == AccountStatus.APPROVED }
                            .flatMap{it.children.filter { it.status == AccountStatus.APPROVED }
                                    .map { SubchildAccountDto(it.id!!, it.fullname, it.status, it.phoneNumber, it.gifts.size, it.parent!!.fullname) }},
                    child.children.filter { it.status == AccountStatus.APPROVED }
                            .flatMap{it.children.filter { it.status == AccountStatus.APPROVED }
                                    .flatMap { it.children.filter { it.status == AccountStatus.APPROVED }
                                            .map { SubchildAccountDto(it.id!!, it.fullname, it.status, it.phoneNumber, it.gifts.size, it.parent!!.fullname)}}}
            )
        }
        return AccountInfo(
                account.id!!,
                account.status,
                account.fullname,
                account.checkNumber,
                account.checkPath,
                account.level,
                account.phoneNumber,
                account.parent?.fullname,
                account.parent?.id,
                children
        )
    }

    private fun formToAccount(account: Account, form: AccountUpdateForm): Account {
        var status = account.status
        if (status != AccountStatus.APPROVED) status = form.checkNumber?.let { AccountStatus.PENDING } ?: AccountStatus.CREATED
        account.status = status
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
        val photoPath = createForm.checkPhoto?.let { fileUploadEndpointService.saveUploadedFile(it) }
        val status = if(photoPath == null && (createForm.checkNumber == null || createForm.checkNumber!!.isBlank())) AccountStatus.CREATED else AccountStatus.PENDING
        return Account(
                status,
                createForm.fullname!!,
                createForm.address!!,
                createForm.checkNumber,
                photoPath,
                createForm.passportNumber,
                createForm.phoneNumber,
                createForm.registeredDate,
                createForm.regionId?.let { regionService.get(it) },
                createForm.parentId?.let { this.get(it) },
                1
        )
    }
}