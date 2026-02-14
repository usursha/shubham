package com.hpy.ops360.sampatti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hpy.ops360.sampatti.entity.AtmCeMapping;

public interface AtmCeMappingRepository extends JpaRepository<AtmCeMapping, Long> {

	@Query(value = "SELECT acm.atm_id FROM atm_ce_mapping acm;", nativeQuery = true)
	List<String> findAllAtmIdsNative();
}
