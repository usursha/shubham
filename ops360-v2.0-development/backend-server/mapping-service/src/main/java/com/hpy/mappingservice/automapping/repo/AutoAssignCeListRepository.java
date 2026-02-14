package com.hpy.mappingservice.automapping.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.mappingservice.automapping.entity.AutoAssignCeListEntity;

public interface AutoAssignCeListRepository extends JpaRepository< AutoAssignCeListEntity, String> {

	 @Query(value = "EXEC USP_Auto_AssignRemainingCE :cmUserId, :excludedCeUserId", nativeQuery = true)
	    List<Object[]> executeAutoAssignRemainingCE(
	        @Param("cmUserId") String cmUserId,
	        @Param("excludedCeUserId") String excludedCeUserId
	    );
}
