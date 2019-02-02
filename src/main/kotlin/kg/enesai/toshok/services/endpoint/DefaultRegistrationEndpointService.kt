package kg.enesai.toshok.services.endpoint

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.domains.User
import kg.enesai.toshok.dtos.AccountDto
import kg.enesai.toshok.dtos.RegisterForm
import kg.enesai.toshok.dtos.UpdateAccountDto
import kg.enesai.toshok.enums.AccountStatus
import kg.enesai.toshok.services.AccountService
import kg.enesai.toshok.services.RegionService
import kg.enesai.toshok.services.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultRegistrationEndpointService(
        private val userService: UserService,
        private val accountservice: AccountService,
        private val regionService: RegionService
) : RegistrationEndpointService {

    @Transactional
    override fun register(form: RegisterForm): AccountDto {
        val user = userService.createMemberUser(form.phoneNumber!!, form.password!!)
        val account = formToAccount(form, user)
        return AccountDto.of(accountservice.create(account))
    }

    private fun formToAccount(dto: RegisterForm, user: User): Account {
        return Account(
                dto.checkNumber?.let { AccountStatus.CREATED } ?: AccountStatus.PENDING,
                dto.fullname!!,
                dto.address!!,
                dto.checkNumber,
                dto.passportNumber,
                dto.phoneNumber,
                dto.registeredDate,
                dto.regionId?.let { regionService.get(it) },
                dto.parentId?.let { accountservice.get(it) },
                user
        )
    }
}