package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.CeMachineUpDownCount;

@Repository
public interface CeMachineUpDownCountRepository extends JpaRepository<CeMachineUpDownCount, Long> {

	@Query(value = "EXEC Usp_get_ATM_Count_for_CE :ce_user_id", nativeQuery = true)
	CeMachineUpDownCount getCeMachineUpDownCount(@Param("ce_user_id") String username);

}
