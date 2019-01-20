package kg.enesai.toshok.services.impl

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.domains.User
import kg.enesai.toshok.dtos.CreateAccountDto
import kg.enesai.toshok.dtos.UpdateAccountDto
import kg.enesai.toshok.enums.AccountStatus
import kg.enesai.toshok.repositories.AccountRepository
import kg.enesai.toshok.services.AccountService
import kg.enesai.toshok.services.RegionService
import kg.enesai.toshok.services.RoleService
import kg.enesai.toshok.services.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultAccountService(
        private val accountRepository: AccountRepository,
        private val regionService: RegionService,
        private val passwordEncoder: PasswordEncoder,
        private val roleService: RoleService,
        private val userService: UserService
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
    override fun create(dto: CreateAccountDto): Account {
        val user = userService.create(createAccountDtoToUser(dto))
        val account = createAccountDtoToAccount(dto, user)
        return accountRepository.save(account)
    }

    @Transactional
    override fun update(id: Int, dto: UpdateAccountDto): Account {
        val account = updateDtoToEntity(id, dto)
        return accountRepository.save(account)
    }

    @Transactional
    override fun delete(id: Int) {
        accountRepository.deleteById(id)
    }

    private fun createAccountDtoToAccount(dto: CreateAccountDto, user: User): Account {
        return Account(
                dto.checkNumber?.let { AccountStatus.CREATED } ?: AccountStatus.PENDING,
                dto.fullname,
                dto.address,
                dto.checkNumber,
                dto.passportNumber,
                dto.phoneNumber,
                dto.registeredDate,
                dto.regionId?.let { regionService.get(it) },
                dto.parentId?.let { get(it) },
                user
        )
    }

    private fun createAccountDtoToUser(dto: CreateAccountDto): User {
        return User(
                dto.phoneNumber,
                passwordEncoder.encode(dto.password),
                roleService.findByName("MEMBER")!!
        )
    }

    private fun updateDtoToEntity(id: Int, dto: UpdateAccountDto): Account {
        val account = get(id)
        account.fullname = dto.fullname
        account.checkNumber = dto.checkNumber
        account.region = dto.regionId?.let { regionService.get(it) }
        account.parent = dto.parentId?.let { get(it) }
        dto.checkNumber?.let { account.status = AccountStatus.PENDING }

        return account
    }
}