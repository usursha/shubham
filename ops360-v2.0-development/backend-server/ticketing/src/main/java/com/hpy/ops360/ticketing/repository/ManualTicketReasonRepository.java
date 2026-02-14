package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.ManualTicketReason;

@Repository
public interface ManualTicketReasonRepository extends JpaRepository<ManualTicketReason, Long> {

	@Query(value = "EXEC USP_GetManualTicketReason", nativeQuery = true)
	List<ManualTicketReason> getManualTicketReason();

}
