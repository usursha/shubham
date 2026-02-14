package com.hpy.mappingservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.mappingservice.entity.ManageUpcomingLeaveCeAtmDetailsEntity;


@Repository
public interface ManageUpcomingLeaveCeAtmDetailsRepository extends JpaRepository<ManageUpcomingLeaveCeAtmDetailsEntity, Long>{
	
	@Query(value = "EXEC [GetTempMappedAtmsByPrimaryCeUsername] :PrimaryCeUsername", nativeQuery = true)
	List<ManageUpcomingLeaveCeAtmDetailsEntity> getUpcomingLeaveCeAtmData(@Param("PrimaryCeUsername") String ceUserId);


}
