package com.hpy.ops360.sampatti.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hpy.ops360.sampatti.entity.ZoneData;

public interface ZoneDataRepository extends JpaRepository<ZoneData, Long> {

	@Query(value="EXEC ops_get_all_zones",nativeQuery = true)
	public List<ZoneData> getAllZones();
}
