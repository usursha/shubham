package com.hpy.mappingservice.automapping.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.automapping.entity.AutoAssignEntity;
@Repository
public interface AutoAssignRepository extends JpaRepository<AutoAssignEntity, String> {

	 @Query(value = "EXEC USP_GetATM_Auto_Assign @OriginalCEUserId = :originalCEUserId", nativeQuery = true)
	    List<Object[]> getAtmAutoAssignByOriginalCEUserId(@Param("originalCEUserId") String originalCEUserId);
	    
}
