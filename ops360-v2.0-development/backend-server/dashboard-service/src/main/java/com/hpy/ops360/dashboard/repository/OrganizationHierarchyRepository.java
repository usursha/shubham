package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.OrganizationHierarchy;
import com.hpy.ops360.dashboard.entity.UserReportingHierarchy;

@Repository
public interface OrganizationHierarchyRepository extends JpaRepository<OrganizationHierarchy, Long> {
	
	@Query(value="select id,username,designation from user_master where username= :username",nativeQuery = true)
	OrganizationHierarchy getHierarchyByUsername(@Param("username") String username);
	
	@Query(value="select Top 1 scm_user_id, rcm_user_id, cm_user_id from atm_ce_mapping where cm_user_id= :username", nativeQuery=true)
	UserReportingHierarchy getUserReportingHead(@Param("username") String username);
}
