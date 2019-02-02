package kg.enesai.toshok.services

import kg.enesai.toshok.domains.User
import kg.enesai.toshok.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultUserService(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder,
        private val roleService: RoleService
) : UserService {
    @Transactional(readOnly = true)
    override fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    @Transactional
    override fun createMemberUser(username: String, password: String): User {
        val user = User(
                username,
                passwordEncoder.encode(password),
                roleService.findByName("MEMBER")!!
        )
        return userRepository.save(user)
    }

}