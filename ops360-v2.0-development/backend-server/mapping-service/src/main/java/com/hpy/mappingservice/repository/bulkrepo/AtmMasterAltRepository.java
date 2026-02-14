package com.hpy.mappingservice.repository.bulkrepo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.bulkentity.AtmMasterAlt;

@Repository
public interface AtmMasterAltRepository extends JpaRepository<AtmMasterAlt, Long> {
    Optional<AtmMasterAlt> findByAtmCode(String atmCode);
}
