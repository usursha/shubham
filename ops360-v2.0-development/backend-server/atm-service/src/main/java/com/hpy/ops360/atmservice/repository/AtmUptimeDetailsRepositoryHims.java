package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.dto.AssignedAtmDetailsDto;
import com.hpy.ops360.atmservice.dto.AssignedAtmDetailsEntityHIMSDto;
import com.hpy.ops360.atmservice.entity.AssignedAtmDetailsEntityHIMS;
import com.hpy.ops360.atmservice.entity.AtmUptimeDetailsHims;


@Repository
public interface AtmUptimeDetailsRepositoryHims extends JpaRepository<AtmUptimeDetailsHims, String> {

	@Query(value = "SELECT top 1 atmid, monthtotilldateuptime, lastmonthuptime FROM atm_uptime_data WHERE atmid = :atmid and date = CONVERT(DATE, DATEADD(DAY, -1, GETDATE()))", 
	           nativeQuery = true)
	    List<AtmUptimeDetailsHims> findByAtmIdNative(@Param("atmid") String atmId);


}