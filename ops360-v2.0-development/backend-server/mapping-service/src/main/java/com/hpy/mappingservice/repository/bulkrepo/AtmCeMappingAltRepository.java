package com.hpy.mappingservice.repository.bulkrepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.bulkentity.AtmCeMappingAlt;

@Repository
public interface AtmCeMappingAltRepository extends JpaRepository<AtmCeMappingAlt, Long> {
    Optional<AtmCeMappingAlt> findByAtmIdAndCeId(Long atmId, Long ceId);
}
