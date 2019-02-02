package kg.enesai.toshok.repositories

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.domains.Region
import kg.enesai.toshok.domains.Role
import kg.enesai.toshok.domains.User
import kg.enesai.toshok.enums.AccountStatus
import org.springframework.data.jpa.repository.JpaRepository

interface RegionRepository : JpaRepository<Region, Int>

interface AccountRepository : JpaRepository<Account, Int> {
    fun countByParentIdAndStatus(parentId: Int, status: AccountStatus): Int
}

interface RoleRepository : JpaRepository<Role, Int> {
    fun findByName(name: String): Role?
}

interface UserRepository : JpaRepository<User, Int>{
    fun findByUsername(username: String): User?
}