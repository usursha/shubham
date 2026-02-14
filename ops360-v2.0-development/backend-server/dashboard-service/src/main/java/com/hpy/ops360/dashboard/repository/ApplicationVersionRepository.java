package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.ApplicationVersion;

@Repository
public interface ApplicationVersionRepository extends JpaRepository<ApplicationVersion, Long> {

	@Query(value = "EXEC USP_GetOPSApplicationMaster :AppPlatform,:ApplicationVersion", nativeQuery = true)
	ApplicationVersion checkUpdateAvailable(@Param("AppPlatform") String appPlatform,
			@Param("ApplicationVersion") String applicationVersion);
}
