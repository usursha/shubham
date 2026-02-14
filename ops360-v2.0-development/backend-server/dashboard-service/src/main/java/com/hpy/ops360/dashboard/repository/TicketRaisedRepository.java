package com.hpy.ops360.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.dashboard.entity.TicketsRaisedEntity;

public interface TicketRaisedRepository extends JpaRepository<TicketsRaisedEntity,Long>{

 	@Query(value = "EXEC USP_GetCmTicketsRaisedCategories :userid",nativeQuery = true)
	public List<TicketsRaisedEntity> getRaisedTicketsCategories(@Param(value = "userid") String userid);  
}
