package com.hpy.ops360.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.dashboard.entity.CEAtmDetailsFor_Cm;

public interface CEAtmDetailsFor_Cm_Repository extends JpaRepository<CEAtmDetailsFor_Cm, Long> {

	@Query(value = "EXEC UPS_GetCM_CEAtmDetails :user_id", nativeQuery = true)
	List<CEAtmDetailsFor_Cm> getCEAtmDetailsByUserId(@Param("user_id") String userId);
}
