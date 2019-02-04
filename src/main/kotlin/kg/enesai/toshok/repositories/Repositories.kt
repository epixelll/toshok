package kg.enesai.toshok.repositories

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.domains.Region
import kg.enesai.toshok.domains.Role
import kg.enesai.toshok.domains.User
import kg.enesai.toshok.enums.AccountStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RegionRepository : JpaRepository<Region, Int> {
    fun findByName(name: String?): Region?
}

interface AccountRepository : JpaRepository<Account, Int> {
    fun countByParentIdAndStatus(parentId: Int, status: AccountStatus): Int
    fun countByParentParentIdAndStatus(parentId: Int, status: AccountStatus): Int
    fun countByParentParentParentIdAndStatus(parentId: Int, status: AccountStatus): Int
    fun countByParentParentParentParentIdAndStatus(parentId: Int, status: AccountStatus): Int
    fun findByFullname(fullname: String?): Account?
    @Modifying
    @Query("update Account set parent.id = null where parent.id = :id")
    fun removeChildsByParentId(@Param("id") id: Int)

    fun findAllByStatus(status: AccountStatus, pageable: Pageable): Page<Account>
}

interface RoleRepository : JpaRepository<Role, Int> {
    fun findByName(name: String): Role?
}

interface UserRepository : JpaRepository<User, Int>{
    fun findByUsername(username: String): User?
}