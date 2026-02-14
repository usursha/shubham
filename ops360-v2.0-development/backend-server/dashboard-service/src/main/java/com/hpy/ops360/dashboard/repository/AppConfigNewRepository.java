package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hpy.ops360.dashboard.entity.AppConfigNew;

public interface AppConfigNewRepository extends JpaRepository<AppConfigNew, Integer> {

	@Query(value = "SELECT TOP 1 * FROM [dbo].[app_config_v1] ORDER BY created_on DESC", nativeQuery = true)
	AppConfigNew findLatestConfig();
}
