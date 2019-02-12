package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.domains.Region
import kg.enesai.toshok.domains.Role
import kg.enesai.toshok.domains.User
import kg.enesai.toshok.dtos.*
import kg.enesai.toshok.enums.Permission
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RegionService {
    fun findAll(): List<RegionDto>
    fun get(id: Int): Region
    fun findByName(name: String?): Region?
}

interface AccountService {
    fun findAll(): List<AccountDto>
    fun findAll(accountSearchDto: AccountSearchDto, pageable: Pageable): Page<AccountDto>
    fun get(id: Int): Account
    fun create(account: Account): Account
    fun createFromExcel(account: Account): Account
    fun create(accountCreateForm: AccountCreateForm): Account
    fun update(form: AccountUpdateForm): Account
    fun delete(id: Int)
    fun getUpdateForm(id: Int): AccountUpdateForm
    fun findByFullname(fullname: String?): Account?
    fun getLevel(account: Account): Int
    fun createAndFlush(account: Account): Account
    fun findAllPending(fullname: String, pageable: Pageable): Page<AccountDto>
    fun approve(id: Int)
    fun findById(id: Int): Account
    fun getAccountInfo(id: Int): AccountInfo
    fun findAllGiftNeededAccounts(accountSearchDto: AccountSearchDto, pageable: Pageable): Page<AccountDto>
    fun createTemporalAccount(name: String): Account
    fun deleteTemporary()
    fun find20ByTerm(term: String): List<AccountSearchResultDto>
//    fun giveGift(id: Int)
}

interface RoleService {
    fun findByName(name: String): Role?
    fun findById(id: Int): Role
    fun findAll(): List<RoleDto>
}

interface UserService {
    fun findByUsername(username: String): User?
    fun createMemberUser(username: String, password: String, account: Account): User
    fun findAllByUsername(username: String, pageable: Pageable): Page<UserDto>
    fun getUpdateForm(id: Int): UserUpdateForm
    fun create(userCreateForm: UserCreateForm)
    fun update(userUpdateForm: UserUpdateForm)
    fun delete(id: Int)
    fun getProfile(): ProfileDto
    fun changePassword(changePasswordForm: ChangePasswordForm)
}

interface CurrentUserService {
    fun getCurrentUser(): User
    fun currentUserHasPermission(permission: Permission): Boolean
}

interface GiftService {
    fun findAllByAccountId(accountId: Int): List<GiftDto>
    fun create(giftCreateForm: GiftCreateForm)
}