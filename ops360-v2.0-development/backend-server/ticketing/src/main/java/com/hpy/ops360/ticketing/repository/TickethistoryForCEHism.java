package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.cm.entity.TicketdetailsEntityHims;

//import com.hpy.ops360.ticketing.dto.TicketDetailsDto;

@Repository
public interface TickethistoryForCEHism extends JpaRepository<TicketdetailsEntityHims, String> {

	/*
	 * @Query(value = "EXEC USP_GetAtmTicketEvent :atmid", nativeQuery = true)
	 * List<AtmHistoryNTicketsResponse> getAtmTicketHistory();
	 */

	//@Query(value = "SELECT * FROM [172.16.15.36].[FLM_CRM_UAT].[dbo].[GetTicketDetails_Open] WHERE equipmentid = :atmid", nativeQuery = true)
	@Query(value = "EXEC USP_FindOpenTicketsDetails :atmid", nativeQuery = true)
	List<TicketdetailsEntityHims> findOpenTicketsByEquipmentId(@Param("atmid") String equipmentId);

}


//	 @Autowired
//	    private JdbcTemplate jdbcTemplate;
//
//	    public List<AtmTicketEvent> getAtmTicketEvent(String username, String atmTicketEventCode) {
//	        return jdbcTemplate.query(
//	            "EXEC USP_GetAtmTicketEvent ?, ?",
//	            new Object[]{username, atmTicketEventCode},
//	            new AtmTicketEventRowMapper()
//	        );
//	    }
//
//	    private static class AtmTicketEventRowMapper implements RowMapper<AtmTicketEvent> {
//	        @Override
//	        public AtmTicketEvent mapRow(ResultSet rs, int rowNum) throws SQLException {
//	            AtmTicketEvent event = new AtmTicketEvent();
//	            event.setAtmId(rs.getString("atm_id"));
//	            event.setTicketId(rs.getString("ticket_id"));
//	            event.setEventCode(rs.getString("eventcode"));
//	            event.setPriorityScore(rs.getDouble("priority_score"));
//	            event.setEventGroup(rs.getString("eventgroup"));
//	            event.setIsBreakdown(rs.getInt("isbreakdown"));
//	            event.setIsUpdated(rs.getInt("is_updated"));
//	            event.setIsTimedOut(rs.getInt("is_timed_out"));
//	            event.setIsTravelling(rs.getInt("is_travelling"));
//	            event.setTravelTime(rs.getDate("travel_time"));
//	            event.setTravelEta(rs.getInt("travel_eta"));
//	            event.setDownCall(rs.getInt("down_call"));
//	            event.setEtaDateTime(rs.getString("eta_date_time"));
//	            event.setOwner(rs.getString("owner"));
//	            event.setEtaTimeout(rs.getString("eta_timeout"));
//	            return event;
//	        }
//	    }
//}
