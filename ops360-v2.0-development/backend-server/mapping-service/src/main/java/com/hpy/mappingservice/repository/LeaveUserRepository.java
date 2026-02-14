package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.LeaveUserEntity;

@Repository
public interface LeaveUserRepository extends JpaRepository<LeaveUserEntity, Long> {

	@Query(value = "EXEC GetApprovedLeaveUsersByManager :managerUsername", nativeQuery = true)
	List<LeaveUserEntity> getApprovedLeaveUsersByManager(@Param("managerUsername") String managerUsername);
}
