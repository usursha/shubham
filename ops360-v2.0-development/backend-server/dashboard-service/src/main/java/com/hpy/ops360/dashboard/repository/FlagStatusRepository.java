package com.hpy.ops360.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.FlagStatus;

@Repository
public interface FlagStatusRepository extends JpaRepository<FlagStatus, Long> {

	@Query(value = "EXEC USP_InsertUpdateFlagstatus :ceUserId, :cmUserId, :flagStatus", nativeQuery = true)
	List<Object[]> callInsertUpdateFlagstatus(@Param("ceUserId") String ceUserId, @Param("cmUserId") String cmUserId,
			@Param("flagStatus") int flagStatus);
}
