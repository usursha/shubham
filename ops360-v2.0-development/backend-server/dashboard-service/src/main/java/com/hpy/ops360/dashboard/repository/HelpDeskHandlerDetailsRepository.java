package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.HelpDeskHandlerDetails;

@Repository
public interface HelpDeskHandlerDetailsRepository extends JpaRepository<HelpDeskHandlerDetails, Long> {

	@Query(value = "EXEC Usp_GetHDEDetails :user_id", nativeQuery = true)
	HelpDeskHandlerDetails getHelpDeskHandlerDetails(@Param("user_id") String ceUserId);

}
