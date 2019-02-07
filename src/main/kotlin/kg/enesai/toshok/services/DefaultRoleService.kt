package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Role
import kg.enesai.toshok.dtos.RegionDto
import kg.enesai.toshok.dtos.RoleDto
import kg.enesai.toshok.repositories.RoleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
class DefaultRoleService(
        private val roleRepository: RoleRepository
) : RoleService {
    @Transactional(readOnly = true)
    override fun findByName(name: String): Role? {
        return roleRepository.findByName(name)
    }

    @Transactional(readOnly = true)
    override fun findById(id: Int) = roleRepository.findById(id).orElseThrow { EntityNotFoundException("Role with id = $id not found") }

    @Transactional
    override fun findAll(): List<RoleDto> {
        return roleRepository.findAll().map { RoleDto.of(it) }
    }
}