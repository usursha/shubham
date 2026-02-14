package com.hpy.mappingservice.repository.bulkrepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.bulkentity.CeToCeMappingAlt;

@Repository
public interface CeToCeMappingAltRepository extends JpaRepository<CeToCeMappingAlt, Integer> {
    boolean existsByAtmIdAndPrimaryCeIdAndMappedCeIdAndActive(String atmId, Integer primaryCeId, Integer mappedCeId, Integer active);
    
    List<CeToCeMappingAlt> findByLeaveRequestId(Long leaveRequestId);
}
