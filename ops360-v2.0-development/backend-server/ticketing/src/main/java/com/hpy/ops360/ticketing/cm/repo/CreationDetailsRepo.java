package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.dto.TicketDetail;
import com.hpy.ops360.ticketing.cm.entity.AllocationETADetails;
import com.hpy.ops360.ticketing.cm.entity.CreationdetailsEntity;
import com.hpy.ops360.ticketing.cm.entity.TicketdetailsEntity;

@Repository
public interface CreationDetailsRepo extends JpaRepository<CreationdetailsEntity, String>{
	
	@Query(value = "EXEC GetCreationDetails :atmid, :ticketno ", nativeQuery = true)
	CreationdetailsEntity getcreationdetails(@Param("atmid") String atmid, @Param("ticketno") String ticketno);

}
