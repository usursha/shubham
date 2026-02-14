package com.hpy.ops360.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.dashboard.entity.OpenTicketHimsView;

public interface OpenTicketHimsViewRepository extends JpaRepository<OpenTicketHimsView, Long> {
	
	
	@Query(value="EXEC usp_get_open_ticket_hims :ce_user_id",nativeQuery = true)
	public List<OpenTicketHimsView> getOpenTicketListHims(@Param("ce_user_id") String ceUserId); 
	
}
