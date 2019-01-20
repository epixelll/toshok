package kg.enesai.toshok.services.impl

import kg.enesai.toshok.domains.User
import kg.enesai.toshok.repositories.UserRepository
import kg.enesai.toshok.services.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultUserService(
        private val userRepository: UserRepository
) : UserService {
    @Transactional(readOnly = true)
    override fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    @Transactional
    override fun create(user: User): User {
        return userRepository.save(user)
    }

}