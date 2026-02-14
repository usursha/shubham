package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.AllAtms;

@Repository
public interface AllAtmsRepository extends JpaRepository<AllAtms,Long> {
	
	@Query(value="EXEC usp_all_atm_list",nativeQuery = true)
	public List<AllAtms> getAllAtmList();
	
	@Query(value="EXEC Usp_PROCESS_get_open_ticket",nativeQuery = true)
	public void executeOpenTicketUniqueListSp();
	
	@Query(value="EXEC usp_get_cm_atm_list :cm_user_id",nativeQuery = true)
	public List<AllAtms> getAllCmAtms(@Param("cm_user_id") String cmUserId);

}
