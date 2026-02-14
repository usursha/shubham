package com.hpy.ops360.ticketing.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.generic.IGenericRepo;
import com.hpy.ops360.ticketing.entity.Travel;
import com.hpy.ops360.ticketing.enums.TravelMode;
import com.hpy.ops360.ticketing.enums.WorkMode;

@Repository
public interface TravelRepository extends IGenericRepo<Travel> {

	@Query(value = "EXEC Usp_udpate_work_travel_details :#{#atSite}, :#{#atmId}, :#{#ticketNo}, :#{#travelEtaDateTime}, :#{#travelMode != null ? #travelMode.toString() : ''}, :#{#travellingToSite}, :#{#workMode != null ? #workMode.toString() : ''}, :#{#username}", nativeQuery = true)
	int addTravelPlan(@Param("atSite") Boolean atSite, @Param("atmId") String atmId, @Param("ticketNo") String ticketNo,
			@Param("travelEtaDateTime") LocalDateTime travelEtaDateTime, @Param("travelMode") TravelMode travelMode,
			@Param("travellingToSite") Boolean travellingToSite, @Param("workMode") WorkMode workMode,
			@Param("username") String username);

	@Query(value = "EXEC Usp_get_travel_mode :username", nativeQuery = true)
	Integer getIsTravellingStatus(@Param("username") String userId);
	
	@Query(value = "EXEC USP_UpdateWorkMode :atm_id,:ticket_no,:work_mode,:user_name",nativeQuery = true)
	Integer updateWorkMode(@Param("atm_id") String atmdId,@Param("ticket_no") String ticketNo,@Param("work_mode") String workMode,@Param("user_name") String username);

}
