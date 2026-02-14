package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.cm.entity.CreationdetailsEntity;


public interface CreationDetailsRepositoryHims extends JpaRepository<CreationdetailsEntity, String> {
	
	@Query(value = "EXEC GetCreationDetailsHIMS :atmid, :ticketno ", nativeQuery = true)
	CreationdetailsEntity getcreationdetailsHims(@Param("atmid") String atmid, @Param("ticketno") String ticketno);

}
