package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.domains.User
import kg.enesai.toshok.dtos.*
import kg.enesai.toshok.enums.Permission
import kg.enesai.toshok.repositories.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException
import javax.persistence.EntityNotFoundException

@Service
class DefaultUserService(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val roleService: RoleService,
        private val accountService: AccountService,
        private val currentUserService: CurrentUserService
) : UserService {
    @Transactional(readOnly = true)
    override fun findByUsername(username: String) = userRepository.findByUsername(username)

    @Transactional
    override fun createMemberUser(username: String, password: String, account: Account): User {
        val user = User(
                username,
                passwordEncoder.encode(password),
                roleService.findByName("MEMBER")!!,
                account
        )
        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun findAllByUsername(username: String, pageable: Pageable) = userRepository.findAllByUsernameIgnoreCaseContaining(username, pageable).map { UserDto.of(it) }

    @Transactional(readOnly = true)
    override fun getUpdateForm(id: Int): UserUpdateForm {
        val user = userRepository.getOne(id)
        return UserUpdateForm.of(user)
    }

    @Transactional
    override fun create(userCreateForm: UserCreateForm) {
        val user: User = formToUser(userCreateForm)
        if(!currentUserService.currentUserHasPermission(Permission.USER_CREATE_ALL) && user.role.name != "MEMBER")
            throw IllegalAccessException("You don't have permission to create user with this role!")
        userRepository.save(user)
    }

    @Transactional
    override fun update(userUpdateForm: UserUpdateForm) {
        var user = userRepository.findById(userUpdateForm.id).orElseThrow { EntityNotFoundException("User with id = ${userUpdateForm.id} is not found") }
        val newRole = userUpdateForm.roleId?.let { roleService.findById(it) } ?: roleService.findByName("MEMBER")!!
        if(!currentUserService.currentUserHasPermission(Permission.USER_UPDATE_ALL) && (user.role.name != "MEMBER" || newRole.name != "MEMBER"))
            throw IllegalAccessException("You don't have permission to create user with this role!")
        user.apply {
            this.username = userUpdateForm.username!!
            userUpdateForm.password?.takeIf { it.isNotBlank() }?.let{ this.password = passwordEncoder.encode(it) }
            this.role = newRole
            this.account = userUpdateForm.accountId?.let { accountService.findById(it) }
        }.apply {
            user = userRepository.save(this)
        }
    }

    @Transactional
    override fun delete(id: Int) {
        val user = userRepository.findById(id).orElseThrow { EntityNotFoundException("User with id = $id is not found") }
        if(!currentUserService.currentUserHasPermission(Permission.USER_DELETE_ALL) && user.role.name != "MEMBER")
            throw IllegalAccessException("You don't have permission to create user with this role!")
        userRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun getProfile() = ProfileDto.of(currentUserService.getCurrentUser())

    @Transactional
    override fun changePassword(changePasswordForm: ChangePasswordForm) {
        val principal = SecurityContextHolder.getContext().authentication.principal as org.springframework.security.core.userdetails.User
        val user = findByUsername(principal.username)!!
        if(!passwordEncoder.matches(changePasswordForm.oldPassword, user.password)) throw IllegalStateException("Old password doesn't match")
        user.password = passwordEncoder.encode(changePasswordForm.newPassword)
        userRepository.save(user)
        SecurityContextHolder.getContext().authentication = null
        SecurityContextHolder.clearContext()
    }

    private fun formToUser(userCreateForm: UserCreateForm) = User(
            userCreateForm.username!!,
            passwordEncoder.encode(userCreateForm.password),
            userCreateForm.roleId?.let { roleService.findById(it) } ?: roleService.findByName("MEMBER")!!,
            userCreateForm.accountId?.let { accountService.findById(it) }
    )
}