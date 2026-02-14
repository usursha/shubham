package com.hpy.monitoringservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.monitoringservice.entity.AllControllerStatsEntity;


@Repository
public interface ControllerstatsRepo extends JpaRepository<AllControllerStatsEntity, Long>{
	
	@Query(value = "EXEC [dbo].[GetControllerStats] :fromDate, :toDate", nativeQuery = true)
	List<AllControllerStatsEntity> getControllerStatsEntity(@Param("fromDate") String fromDate, @Param("toDate") String toDate);

}
