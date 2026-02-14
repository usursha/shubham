//package com.hpy.ops360.ticketing.cm.repo;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.hpy.ops360.ticketing.cm.entity.AllocationETADetails;
//
//@Repository
//public interface AllocationEtaDetailsRepository extends JpaRepository<AllocationETADetails, Long> {
//
//	@Query(value = "EXEC USP_allocation_eta_details :ticket_number, :atm_id", nativeQuery = true)
//	List<Object[]> getallocationDetails(@Param("ticket_number") String ticket_number, @Param("atm_id") String atm_id);
//}

package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.AllocationETADetails;

@Repository
public interface AllocationEtaDetailsRepository extends JpaRepository<AllocationETADetails, Long> {

	@Query(value = "EXEC USP_allocation_eta_details :ticket_number, :atm_id", nativeQuery = true)
	List<AllocationETADetails> getallocationDetails(@Param("ticket_number") String ticket_number, @Param("atm_id") String atm_id);
}

