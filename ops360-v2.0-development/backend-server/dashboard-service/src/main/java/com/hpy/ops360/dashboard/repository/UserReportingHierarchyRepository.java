package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.UserReportingHierarchy;

@Repository
public interface UserReportingHierarchyRepository extends JpaRepository<UserReportingHierarchy, Long> {

	@Query(value="select Top 1 sr_no,scm_user_id, rcm_user_id, cm_user_id from atm_ce_mapping where cm_user_id= :username", nativeQuery=true)
	UserReportingHierarchy getUserReportingHead(@Param("username") String username);

	@Query(value="select count(distinct(ce_user_id)) from atm_ce_mapping where cm_user_id='username';", nativeQuery=true)
	Long getTotalCEAssigned(@Param("username") String username);
	
	@Query(value="select count(atm_id) from atm_ce_mapping where cm_user_id= :username", nativeQuery=true)
	Long getTotalAtmAssigned(@Param("username") String username);
	
	@Query(value="select employee_code from user_master where username= :username", nativeQuery=true)
	String getEmployeeIdByUsername(@Param("username") String username);
	
	@Query(value="select email_id from user_master where username= :username", nativeQuery=true)
	String getEmailIdByUsername(@Param("username") String username);
	
	@Query(value="select full_name from user_master where username= :username", nativeQuery=true)
	String getfullnamedByUsername(@Param("username") String username);
	
	@Query(value="select mobileno from user_master where username= :username", nativeQuery=true)
	Long getUserMobiledByUsername(@Param("username") String username);
	
	@Query(value="select home_address from user_master where username= :username", nativeQuery=true)
	String getHomeAddressdByUsername(@Param("username") String username);
	
	

}
