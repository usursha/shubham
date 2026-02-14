package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.TicketHistoryEntity;

@Repository
public interface TicketHistoryRepository extends JpaRepository<TicketHistoryEntity, Long> {

//	@Query(value = "EXEC ticket_history :userId,:pageIndex ,:pageSize", nativeQuery = true)
//	List<TicketHistoryEntity> getTicketHistoryDetails(@Param("userId") String userId,
//			@Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize);
	
	@Query(value = "EXEC ticket_history :cm_user_id, :pageNumber, :pageSize, :flag, :searchText", 
	           nativeQuery = true)
	    List<TicketHistoryEntity> getTicketHistoryDetails(@Param("cm_user_id") String cmUserId, 
	                                  @Param("pageNumber") Integer pageNumber,
	                                  @Param("pageSize") Integer pageSize, 
	                                  @Param("flag") String flag, 
	                                  @Param("searchText") String searchText);
}

