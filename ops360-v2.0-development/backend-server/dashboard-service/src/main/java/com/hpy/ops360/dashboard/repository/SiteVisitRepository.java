package com.hpy.ops360.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.SiteVisits;

@Repository
public interface SiteVisitRepository extends JpaRepository<SiteVisits, Long> {

	@Query(value = "EXEC Usp_get_site_visits :username", nativeQuery = true)
	List<SiteVisits> getSiteVisitDetails(@Param("username") String userId);

}
