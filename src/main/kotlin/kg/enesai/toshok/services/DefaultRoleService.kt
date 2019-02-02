package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Role
import kg.enesai.toshok.repositories.RoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultRoleService(
        private val roleRepository: RoleRepository
) : RoleService {
    @Transactional(readOnly = true)
    override fun findByName(name: String): Role? {
        return roleRepository.findByName(name)
    }

}