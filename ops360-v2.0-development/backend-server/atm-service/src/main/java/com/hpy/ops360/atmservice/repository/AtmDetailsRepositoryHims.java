package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.dto.AssignedAtmDetailsDto;
import com.hpy.ops360.atmservice.dto.AssignedAtmDetailsEntityHIMSDto;
import com.hpy.ops360.atmservice.entity.AssignedAtmDetailsEntityHIMS;


@Repository
public interface AtmDetailsRepositoryHims extends JpaRepository<AssignedAtmDetailsEntityHIMS, String> {

	@Query(value = "EXEC GetATMDetailsById @atmIdParam = :atmIdParam", nativeQuery = true)
	public List<AssignedAtmDetailsEntityHIMS> getAtmDetailsHims(@Param("atmIdParam") String atmId);

}