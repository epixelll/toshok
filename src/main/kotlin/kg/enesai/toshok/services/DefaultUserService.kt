package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.domains.User
import kg.enesai.toshok.dtos.*
import kg.enesai.toshok.repositories.UserRepository
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
        private val accountService: AccountService
) : UserService {
    @Transactional(readOnly = true)
    override fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

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
    override fun findAll(pageable: Pageable) = userRepository.findAll(pageable).map { UserDto.of(it) }

    @Transactional(readOnly = true)
    override fun getUpdateForm(id: Int): UserUpdateForm {
        val user = userRepository.getOne(id)
        return UserUpdateForm.of(user)
    }

    @Transactional
    override fun create(userCreateForm: UserCreateForm) {
        val user: User = formToUser(userCreateForm)
        userRepository.save(user)
    }

    @Transactional
    override fun update(userUpdateForm: UserUpdateForm) {
        var user = userRepository.findById(userUpdateForm.id).orElseThrow { EntityNotFoundException("User with id = ${userUpdateForm.id} is not found") }
        user.apply {
            this.username = userUpdateForm.username!!
            userUpdateForm.password?.takeIf { it.isNotBlank() }?.let{ this.password = passwordEncoder.encode(it) }
            this.role = roleService.findById(userUpdateForm.roleId!!)
            this.account = userUpdateForm.accountId?.let { accountService.findById(it) }
        }.apply {
            user = userRepository.save(this)
        }
    }

    @Transactional
    override fun delete(id: Int) {
        userRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun getProfile(): ProfileDto {
        val principal = SecurityContextHolder.getContext().authentication.principal as org.springframework.security.core.userdetails.User
        val user = findByUsername(principal.username)!!
        return ProfileDto.of(user)
    }

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
            roleService.findById(userCreateForm.roleId!!),
            userCreateForm.accountId?.let { accountService.findById(it) }
    )
}