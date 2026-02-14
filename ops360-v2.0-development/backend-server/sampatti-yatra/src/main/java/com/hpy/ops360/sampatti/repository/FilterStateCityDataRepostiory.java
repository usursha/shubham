package com.hpy.ops360.sampatti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.sampatti.entity.FilterStateCityData;

public interface FilterStateCityDataRepostiory extends JpaRepository<FilterStateCityData, Long> {
	
	@Query(value="EXEC ops_get_zone_state_city_filter_data :selectedZone,:selectedState,:selectedCity,:designation",nativeQuery = true)
	public List<FilterStateCityData> getFilterStateCityData(@Param("selectedZone") String selectedZone, @Param("selectedState") String selectedState,@Param("selectedCity") String selectedCity,@Param("designation") String role);
}
