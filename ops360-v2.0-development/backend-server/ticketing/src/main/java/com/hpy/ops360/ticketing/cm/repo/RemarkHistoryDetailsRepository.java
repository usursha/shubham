package com.hpy.ops360.ticketing.cm.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.RemarkHistoryDetailsNew;


@Repository
public interface RemarkHistoryDetailsRepository extends JpaRepository<RemarkHistoryDetailsNew, Long> {

	  @Query(value = "EXEC GetTicketRemarkHistoryDetails :ticketNo, :atmId, :pageNumber, :pageSize", nativeQuery = true)
	    List<RemarkHistoryDetailsNew> getRemarkHistoryforCm(@Param("ticketNo") String ticketNo,@Param("atmId") String atmId,
	    		 @Param("pageNumber") Integer pageNumber,
                 @Param("pageSize") Integer pageSize);
}

