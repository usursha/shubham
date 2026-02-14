package com.hpy.mappingservice.repository;

import java.util.List;

import org.apache.catalina.manager.DummyProxySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.DummyEntity;

@Repository
public interface ATMAssignmentRepository extends JpaRepository<DummyEntity, Long> {

	@Query(value = "EXEC GetATMCEMappingDetails :cmUserId, :ceUserId", nativeQuery = true)
	List<Object[]> getATMCEMappingDetails(@Param("cmUserId") String cmUserId, @Param("ceUserId") String ceUserId);

	@Query(value = "EXEC sp_GetCEUserLocations :cmUserId, :excludedCeUserId", nativeQuery = true)
	List<Object[]> getCEUserLocations(@Param("cmUserId") String cmUserId,
			@Param("excludedCeUserId") String excludedCeUserId);

	// New query to get single ATM coordinates by ATM ID
	@Query(value = "SELECT latitude, longitude FROM atm_master WHERE atm_code = :atmId", nativeQuery = true)
	List<Object[]> getATMCoordinates(@Param("atmId") String atmId);

	@Query(value = "SELECT ce_user_id, cm_user_id FROM atm_ce_mapping WHERE atm_id = :atmId", nativeQuery = true)
	List<Object[]> getCurrentATMAssignment(@Param("atmId") String atmId);

}