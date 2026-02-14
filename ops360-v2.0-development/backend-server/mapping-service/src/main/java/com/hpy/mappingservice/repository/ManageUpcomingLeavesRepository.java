package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.ManageUpcomingLeavesEntity;

@Repository
	public interface ManageUpcomingLeavesRepository extends JpaRepository<ManageUpcomingLeavesEntity, Long> {
	@Query(value ="EXEC GetUpcomingLeaveUsersByManager :ManagerUsername", nativeQuery = true)
	    List<ManageUpcomingLeavesEntity> getUpcomingLeavesCeListData(@Param("ManagerUsername") String cmID);
	
	@Query(value ="EXEC  [GetUpcomingLeaveDetailsByCeUsername] :CEUsername", nativeQuery = true)
    List<ManageUpcomingLeavesEntity> getUpcomingLeavesCeDetails(@Param("CEUsername") String ceUserId);
	    
	}
