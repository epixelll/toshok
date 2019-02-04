package kg.enesai.toshok.dtos

import kg.enesai.toshok.domains.Region

data class RegionDto(
        val id: Int,
        val name: String
) {
    companion object {
        fun of(region: Region) = RegionDto(
                region.id,
                region.name
        )
    }
}