package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.MTDUptimeHims;

public interface MTDUptimeHimsRepository extends JpaRepository<MTDUptimeHims, Long> {
	
	@Query(value="EXEC get_atm_uptime_hims :atmid",nativeQuery = true)
	public MTDUptimeHims getAtmUptimeHims(@Param("atmid") String atmId);

}
