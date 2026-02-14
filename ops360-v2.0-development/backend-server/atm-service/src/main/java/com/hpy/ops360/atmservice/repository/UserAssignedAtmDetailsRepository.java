package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.UserAssignedAtmDetails;

@Repository
public interface UserAssignedAtmDetailsRepository extends JpaRepository<UserAssignedAtmDetails, Long> {
	//get these details from atm_master table
	@Query(value = "EXEC OPS_GetATMDetails :user_login_id", nativeQuery = true)
	List<UserAssignedAtmDetails> getUserAssignedAtmDetails(@Param("user_login_id") String user_login_id);
}
