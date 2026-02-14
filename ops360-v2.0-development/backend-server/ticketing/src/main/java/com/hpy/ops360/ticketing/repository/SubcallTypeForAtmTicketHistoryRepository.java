package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hpy.ops360.ticketing.entity.SubcallTypeEntityForAtmTicketHistory;

public interface SubcallTypeForAtmTicketHistoryRepository
		extends JpaRepository<SubcallTypeEntityForAtmTicketHistory, Long> {

	@Query(value = "EXEC dbo.Usp_get_Owner_List_filter :broad_category", nativeQuery = true)
	List<Object[]> getOwnerListByBroadCategory(@Param("broad_category") String broadCategory);

	
}
