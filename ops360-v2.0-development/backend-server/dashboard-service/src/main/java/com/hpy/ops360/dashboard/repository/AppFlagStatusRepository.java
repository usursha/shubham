package com.hpy.ops360.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.dashboard.entity.AppFlagStatus;

public interface AppFlagStatusRepository extends JpaRepository<AppFlagStatus, Long> {
	
	@Query(value="EXEC usp_get_app_flag_status :ce_username",nativeQuery = true)
	public List<AppFlagStatus> getAppFlagStatus(@Param("ce_username") String ceUsername);

}
