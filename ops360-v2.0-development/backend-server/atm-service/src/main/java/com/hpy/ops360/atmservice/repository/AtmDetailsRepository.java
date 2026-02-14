package com.hpy.ops360.atmservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.AtmDetails;

@Repository
public interface AtmDetailsRepository extends JpaRepository<AtmDetails, Long> {

	@Query(value = "EXEC USP_GetCEAtmDetails @atmid = :atmid", nativeQuery = true)
	AtmDetails getAtmDetails(@Param("atmid") String atmId);

}
