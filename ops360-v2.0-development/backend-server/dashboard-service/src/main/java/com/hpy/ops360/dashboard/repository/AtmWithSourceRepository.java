package com.hpy.ops360.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.dashboard.entity.AtmWithSource;

public interface AtmWithSourceRepository extends JpaRepository<AtmWithSource, Long> {
	
	@Query(value="EXEC get_ce_atm_with_source :ce_user_id",nativeQuery = true)
	public List<AtmWithSource> getAtmWithSourceForCe(@Param("ce_user_id") String ceUserName);

}
