package kg.enesai.toshok.services

import kg.enesai.toshok.domains.User
import kg.enesai.toshok.enums.Permission
import kg.enesai.toshok.repositories.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class DefaultCurrentUserService(
        private val userRepository: UserRepository
): CurrentUserService {
    override fun getCurrentUser(): User{
        val principal = SecurityContextHolder.getContext().authentication.principal as org.springframework.security.core.userdetails.User
        return userRepository.findByUsername(principal.username)!!
    }

    override fun currentUserHasPermission(permission: Permission) = getCurrentUser().role.permissions.contains(permission)
}