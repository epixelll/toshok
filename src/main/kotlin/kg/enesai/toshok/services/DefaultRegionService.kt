package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Region
import kg.enesai.toshok.dtos.RegionDto
import kg.enesai.toshok.repositories.RegionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultRegionService(
        private val regionRepository: RegionRepository
): RegionService {
    @Transactional
    override fun findAll(): List<RegionDto> {
        return regionRepository.findAll().map { RegionDto.of(it) }
    }

    @Transactional(readOnly = true)
    override fun get(id: Int): Region {
        return regionRepository.getOne(id)
    }

    @Transactional(readOnly = true)
    override fun findByName(name: String?): Region? {
        return regionRepository.findByName(name)
    }
}