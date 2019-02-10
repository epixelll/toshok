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
        private val regionService: RegionService,
        private val fileUploadEndpointService: FileUploadEndpointService
) : RegistrationEndpointService {

    var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun import(file: InputStreamSource) {
        readCsv(file.inputStream).forEach{ accountCell ->
            val account = cellToAccount(accountCell)
            account?.let { accountservice.createFromExcel(it) }
        }
//        accountservice.deleteTemporary()
    }

    private fun cellToAccount(cell: AccountCell): Account? {
        if(cell.fullname == "Аты, жөнү" || cell.fullname == null || cell.fullname!!.isBlank() || cell.fullname == "0") return null
        var date: LocalDate? = null
        try{date = cell.registeredDate?.let{LocalDate.parse(it, formatter)}}
        catch (ex: DateTimeParseException) {}
        var parent = cell.parentName?.takeIf { it.isNotBlank() && it != "0" }?.let {
            accountservice.findByFullname(cell.parentName?.trim()) ?: accountservice.createTemporalAccount(it)
        }
        if(hasParentConflict(parent, cell.fullname?.trim()!!)) parent = null
        return accountservice.findByFullname(cell.fullname?.trim())?.let {
             it.apply {
                this.status = AccountStatus.APPROVED
                this.address = cell.address?.takeIf{ it.isNotBlank() }?.let { it.trim() }
                this.checkNumber = cell.checkNumber?.takeIf{ it.isNotBlank() }?.let { it.trim() }
                this.passportNumber = cell.passportNumer?.takeIf{ it.isNotBlank() }?.let { it.trim() }
                this.phoneNumber = cell.phoneNumber?.takeIf{ it.isNotBlank() }?.let { it.trim() }
                this.registeredDate = date
                this.region = regionService.findByName(cell.address)
                this.parent = parent
            }
        } ?: Account(
                AccountStatus.APPROVED,
                cell.fullname!!.trim(),
                cell.address?.takeIf{ it.isNotBlank() }?.let { it.trim() },
                cell.checkNumber?.takeIf{ it.isNotBlank() }?.let { it.trim() },
                null,
                cell.passportNumer?.takeIf{ it.isNotBlank() }?.let { it.trim() },
                cell.phoneNumber?.takeIf{ it.isNotBlank() }?.let { it.trim() },
                date,
                regionService.findByName(cell.address),
                parent,
                1
        )
    }

    private fun hasParentConflict(parent: Account?, fullname: String): Boolean {
        var nextParent = parent
        while(nextParent != null){
            if(nextParent.fullname == fullname) return true
            nextParent = nextParent.parent
        }
        return false
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
        val photoPath = dto.checkPhoto?.let { fileUploadEndpointService.saveUploadedFile(it) }
        val status = if(photoPath == null && (dto.checkNumber == null || dto.checkNumber!!.isBlank())) AccountStatus.CREATED else AccountStatus.PENDING
        return Account(
                status,
                dto.fullname!!,
                dto.address!!,
                dto.checkNumber,
                photoPath,
                dto.passportNumber,
                dto.phoneNumber,
                LocalDate.now(),
                dto.regionId?.let { regionService.get(it) },
                dto.parentId?.let { accountservice.get(it) },
                1
        )
    }
}