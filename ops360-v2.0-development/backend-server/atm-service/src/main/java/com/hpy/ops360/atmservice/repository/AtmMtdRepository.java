package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.entity.AtmMtdEntity;

@Repository
public interface AtmMtdRepository extends JpaRepository<AtmMtdEntity, Long>{
	
	@Query(value = "EXEC usp_atm_month_week_mtd_target :atm_id, :type_date", nativeQuery = true) 
	public List<AtmMtdEntity> getAtmMTD(
			@Param("atm_id") String atmId, 
			@Param("type_date") String  typeDate); 
}
