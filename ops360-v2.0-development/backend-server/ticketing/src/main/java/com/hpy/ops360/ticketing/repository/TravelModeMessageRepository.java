package com.hpy.ops360.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.TravelModeMessage;

public interface TravelModeMessageRepository extends JpaRepository<TravelModeMessage, Long> {

	@Query(value = "EXEC Usp_get_travel_mode_message :username", nativeQuery = true)
	TravelModeMessage getTravelModeMessage(@Param("username") String userId);

}
