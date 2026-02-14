package com.hpy.ops360.atmservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.dto.AtmIdDto;
import com.hpy.ops360.atmservice.entity.TicketDetailsHimsEntity;


@Repository
public interface AtmDetailsRepoHims extends JpaRepository<TicketDetailsHimsEntity, String> {

//	 @Query(value = "SELECT * FROM GetTicketDetails_All WHERE equipmentid = :equipmentId", 
//	           nativeQuery = true)
//	    List<TicketDetailsHimsEntity> findByEquipmentIdNative(@Param("equipmentId") String atmId);

	@Query(value = "EXEC GetTicketDetailsByAtmIdHims :equipmentId", nativeQuery = true)
    List<TicketDetailsHimsEntity> findByEquipmentIdNative(@Param("equipmentId") String atmId);

	
}