package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.AllocationDetails;

@Repository
public interface AllocationDetailsRepository extends JpaRepository<AllocationDetails, Long> {

	@Query(value = "EXEC USP_allocation_eta_details_update :ticket_number, :atm_id", nativeQuery = true)
	List<AllocationDetails> getallocationDetails(@Param("ticket_number") String ticket_number, @Param("atm_id") String atm_id);
}

