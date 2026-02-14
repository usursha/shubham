package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.MTDUptimeDetails;


public interface MTDUptimeDetailsRepository extends JpaRepository<MTDUptimeDetails, Long> {
	
	@Query(value="EXEC get_atm_uptime :atmid",nativeQuery = true)
	public MTDUptimeDetails getAtmUptime(@Param("atmid") String atmId);
}
