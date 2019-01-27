package kg.enesai.toshok.services

import kg.enesai.toshok.domains.Region
import kg.enesai.toshok.repositories.RegionRepository
import kg.enesai.toshok.services.RegionService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DefaultRegionService(
        private val regionRepository: RegionRepository
): RegionService{
    @Transactional
    override fun findAll(): List<Region> {
        return regionRepository.findAll()
    }

    @Transactional(readOnly = true)
    override fun get(id: Int): Region {
        return regionRepository.getOne(id)
    }
}