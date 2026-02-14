package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.UserAtmDetails;

@Repository
public interface UserAtmDetailsRepository extends JpaRepository<UserAtmDetails, Long> {

	@Query(value = "EXEC OPS_GetAtmDetailFromUser_hims :user_login_id", nativeQuery = true)
	List<UserAtmDetails> getUserAtmDetails(@Param("user_login_id") String userId);
}
