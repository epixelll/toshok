package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Account
import kg.enesai.toshok.domains.Region
import kg.enesai.toshok.domains.Role
import kg.enesai.toshok.domains.User
import kg.enesai.toshok.dtos.CreateAccountDto
import kg.enesai.toshok.dtos.UpdateAccountDto

interface RegionService {
    fun findAll(): List<Region>
    fun get(id: Int): Region
}

interface AccountService {
    fun findAll(): List<Account>
    fun get(id: Int): Account
    fun create(dto: CreateAccountDto): Account
    fun update(id: Int, dto: UpdateAccountDto): Account
    fun delete(id: Int)
}

interface RoleService {
    fun findByName(name: String): Role?
}

interface UserService {
    fun findByUsername(username: String): User?
    fun createMemberUser(username: String, password: String): User
}