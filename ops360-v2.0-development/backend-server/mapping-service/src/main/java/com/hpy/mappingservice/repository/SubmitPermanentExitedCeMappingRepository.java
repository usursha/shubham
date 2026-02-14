package com.hpy.mappingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.bulkentity.CeToCeMappingAlt;


@Repository
public interface SubmitPermanentExitedCeMappingRepository extends JpaRepository<CeToCeMappingAlt, Long> {

//	 boolean permamentExistsByAtmIdAndPrimaryCeIdAndMappedCeIdAndActive(String atmId, Integer primaryCeId, Integer mappedCeId, Integer active);
	// boolean permamentExistsByAtmIdAndPrimaryCeIdAndMappedCeIdAndActive(String atmId, Integer primaryCeId, Integer mappedCeId, Integer active);


}
