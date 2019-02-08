package kg.enesai.toshok.services.endpoint

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.domains.User
import kg.enesai.toshok.dtos.AccountDto
import kg.enesai.toshok.dtos.RegisterForm
import kg.enesai.toshok.enums.AccountStatus
import kg.enesai.toshok.services.AccountService
import kg.enesai.toshok.services.RegionService
import kg.enesai.toshok.services.UserService
import org.springframework.core.io.InputStreamSource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import com.opencsv.bean.CsvToBean
import com.opencsv.bean.ColumnPositionMappingStrategy
import kg.enesai.toshok.dtos.AccountCell
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Service
class DefaultRegistrationEndpointService(
        private val userService: UserService,
        private val accountservice: AccountService,
        private val regionService: RegionService
) : RegistrationEndpointService {

    var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun import(file: InputStreamSource) {
        readCsv(file.inputStream).forEach{ accountCell ->
            val account = cellToAccount(accountCell)
            account?.let { accountservice.create(it) }
        }
    }

    private fun cellToAccount(cell: AccountCell): Account? {
        if(cell.fullname == "Аты, жөнү" || cell.fullname == null || cell.fullname!!.isBlank() || cell.fullname == "0") return null
        var date: LocalDate? = null
        try{date = cell.registeredDate?.let{LocalDate.parse(it, formatter)}}
        catch (ex: DateTimeParseException) {}
        return Account(
                AccountStatus.APPROVED,
                cell.fullname!!.trim(),
                cell.address?.takeIf{ it.isNotBlank() }?.let { it.trim() },
                cell.checkNumber?.takeIf{ it.isNotBlank() }?.let { it.trim() },
                cell.passportNumer?.takeIf{ it.isNotBlank() }?.let { it.trim() },
                cell.phoneNumber?.takeIf{ it.isNotBlank() }?.let { it.trim() },
                date,
                regionService.findByName(cell.address),
                accountservice.findByFullname(cell.parentName),
                1,
                0
        )
    }

    @Transactional
    override fun register(form: RegisterForm): AccountDto {
        var account = formToAccount(form)
        account = accountservice.create(account)
        userService.createMemberUser(form.phoneNumber!!, form.password!!, account)
        return AccountDto.of(account)
    }

    @Throws(Exception::class)
    fun readCsv(inputstream: InputStream): List<AccountCell> {
        val ms = ColumnPositionMappingStrategy<AccountCell>()
        ms.type = AccountCell::class.java
//        val columns = arrayOf("fullname", "address", "checkNumber", "passportNumer", "registeredDate", "parentName", "phoneNumber")

        val reader = BufferedReader(InputStreamReader(inputstream))
        val csvToBean = CsvToBean<AccountCell>()

        return csvToBean.parse(ms, reader)
    }

    private fun formToAccount(dto: RegisterForm): Account {
        return Account(
                dto.checkNumber?.let { AccountStatus.PENDING } ?: AccountStatus.CREATED,
                dto.fullname!!,
                dto.address!!,
                dto.checkNumber,
                dto.passportNumber,
                dto.phoneNumber,
                LocalDate.now(),
                dto.regionId?.let { regionService.get(it) },
                dto.parentId?.let { accountservice.get(it) },
                1,
                0
        )
    }
}