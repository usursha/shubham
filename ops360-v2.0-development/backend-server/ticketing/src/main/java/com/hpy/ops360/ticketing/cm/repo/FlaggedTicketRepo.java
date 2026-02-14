package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.dto.TicketDetail;
import com.hpy.ops360.ticketing.cm.entity.AllocationETADetails;
import com.hpy.ops360.ticketing.cm.entity.TicketdetailsEntity;

@Repository
public interface FlaggedTicketRepo extends JpaRepository<TicketdetailsEntity, Long>{
	
	@Query(value = "EXEC usp_flag_ticket_history :user_id, :user_type", nativeQuery = true)
	List<TicketdetailsEntity> getflaggedticketdetails(@Param("user_id") String user_id, @Param("user_type") String user_type);

}
