package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.CheckAtmSource;

public interface CheckAtmSourceRepository extends JpaRepository<CheckAtmSource, Long> {
	
	@Query(value="EXEC usp_GetAtmSourceCode :atmCodeList",nativeQuery = true)
	public List<CheckAtmSource> checkAtmSource(@Param("atmCodeList") String atmCodeList);
	

}
