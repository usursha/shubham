package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.MtdUptime;

@Repository
public interface MtdUptimeRepository extends JpaRepository<MtdUptime, Long> {

	@Query(value = "EXEC USP_GetCEUptime :ce_user_id", nativeQuery = true)
	MtdUptime getMtdUptimeFromSp(@Param("ce_user_id") String ceUsername);
}
