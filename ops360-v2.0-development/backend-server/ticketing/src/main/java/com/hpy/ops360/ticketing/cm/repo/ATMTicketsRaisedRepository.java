package com.hpy.ops360.ticketing.cm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.ATMTicketsRaisedCount;

@Repository
public interface ATMTicketsRaisedRepository extends JpaRepository<ATMTicketsRaisedCount, Long> {


	@Query(value = "EXEC USP_GetATMTicketsRaisedCount :atmId", nativeQuery = true)
	ATMTicketsRaisedCount getATMTicketsCount(@Param("atmId") String atmId);

}
