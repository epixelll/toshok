package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.Role

data class RoleDto(
        val id: Int,
        val name: String
) {
    companion object {
        fun of(role: Role) = RoleDto(
                role.id,
                role.name
        )
    }
}