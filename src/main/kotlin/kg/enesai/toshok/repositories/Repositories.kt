package kg.enesai.toshok.repositories

import kg.enesai.toshok.domains.*
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
    fun findByFullname(fullname: String?): List<Account>
    @Modifying
    @Query("update Account set parent.id = null where parent.id = :id")
    fun removeChildsByParentId(@Param("id") id: Int)

    @Query(value ="SELECT a from Account a where lower(a.fullname) like concat('%', lower(:fullname), '%') " +
            "and (:regionId = null or a.region.id = :regionId) " +
            "and a.level > (SELECT count(g) from Gift g where g.account = a) and a.status = 'APPROVED'")
    fun findAllGiftNeededAccounts(@Param("fullname") fullname: String, regionId: Int?, pageable: Pageable): Page<Account>

    @Query(value = "SELECT a from Account a where lower(a.fullname) like concat('%', lower(:fullname), '%') " +
            "and (null = :status OR a.status = :status) " +
            "and (null = :level OR a.level = :level) " +
            "and (null = :regionId OR a.region.id = :regionId) ")
    fun search(fullname: String?, status: AccountStatus?, level: Int?, regionId: Int?, pageable: Pageable): Page<Account>

    fun deleteByStatus(status: AccountStatus)
    fun findAllByFullnameIgnoreCaseContainingAndStatusAndLevel(fullname: String, status: AccountStatus, level: Int, pageable: Pageable): Page<Account>
    fun findAllByFullnameIgnoreCaseContainingAndStatus(fullname: String, status: AccountStatus, pageable: Pageable): Page<Account>
    fun findAllByFullnameIgnoreCaseContainingAndLevel(fullname: String, level: Int, pageable: Pageable): Page<Account>
    fun findAllByFullnameIgnoreCaseContaining(fullname: String, pageable: Pageable): Page<Account>
    fun countAllByParentIdAndStatus(id: Int, approved: AccountStatus): Int
}

interface RoleRepository : JpaRepository<Role, Int> {
    fun findByName(name: String): Role?
}

interface UserRepository : JpaRepository<User, Int>{
    fun findByUsername(username: String): User?
    fun findAllByUsernameIgnoreCaseContaining(username: String, pageable: Pageable): Page<User>
    fun deleteByAccount(account: Account)
}

interface GiftRepository: JpaRepository<Gift, Int>{
    fun findAllByAccountId(accountId: Int): List<Gift>
}