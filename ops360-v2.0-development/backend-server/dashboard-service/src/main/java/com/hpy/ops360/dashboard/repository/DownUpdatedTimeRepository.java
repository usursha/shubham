package com.hpy.ops360.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.DownUpdatedTimeResponse;

@Repository
public interface DownUpdatedTimeRepository extends JpaRepository<DownUpdatedTimeResponse, Long> {

	@Query(value = "EXEC USP_GetDownUpdatedTime :user_id", nativeQuery = true)
	DownUpdatedTimeResponse getDownUpdatedTime(@Param("user_id") String userId);
}
