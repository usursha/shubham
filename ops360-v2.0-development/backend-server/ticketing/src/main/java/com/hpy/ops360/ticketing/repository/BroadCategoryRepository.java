package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.BroadCategory;

@Repository
public interface BroadCategoryRepository extends JpaRepository<BroadCategory, Long>{
	
	
	@Query(value="EXEC Usp_Get_Broad_Category :username,:ticket_number,:atm_id,:sub_call",nativeQuery = true)
	List<BroadCategory> getBroadCategory(@Param("username") String userId,@Param("sub_call") String subcallType,@Param("ticket_number") String ticketNumber,@Param("atm_id") String atmId);

}//get broad category owner
