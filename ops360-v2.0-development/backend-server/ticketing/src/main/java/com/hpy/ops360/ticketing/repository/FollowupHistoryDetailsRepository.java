package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.RemarkHistoryDetails;

@Repository
public interface FollowupHistoryDetailsRepository extends JpaRepository<RemarkHistoryDetails, Long> {
    
//    @Query(value = "SELECT * FROM GetFollowup_History_Details WHERE ticketno = :ticketNo AND atmid = :atmId ORDER BY date DESC", 
//           nativeQuery = true)
//    List<FollowupHistoryDetails> getFollowupHistoryByTicketAndAtm(@Param("ticketNo") String ticketNo, 
//                                                                  @Param("atmId") String atmId);
//    
    
    
    
 // Updated method using stored procedure
    @Query(value = "EXEC GetFollowupHistoryByTicketAndAtm :ticketNo, :atmId", nativeQuery = true)
    List<RemarkHistoryDetails> getFollowupHistoryByTicketAndAtm(@Param("ticketNo") String ticketNo,
                                                                                  @Param("atmId") String atmId);
    
 // Updated method using stored procedure
    @Query(value = "EXEC GetFollowupHistoryWithOwnerByTicketAndAtmCm :ticketNo, :atmId", nativeQuery = true)
    List<RemarkHistoryDetails> getFollowupHistoryWithOwnerByTicketAndAtmCm(@Param("ticketNo") String ticketNo,
                                                                                  @Param("atmId") String atmId);
}