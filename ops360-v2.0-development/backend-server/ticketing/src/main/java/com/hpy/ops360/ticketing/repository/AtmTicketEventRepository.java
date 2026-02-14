package com.hpy.ops360.ticketing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.entity.AtmTicketEvent;

@Repository
public interface AtmTicketEventRepository extends JpaRepository<AtmTicketEvent, Long> {

	@Query(value = "EXEC USP_GetAtmTicketEvent :username,:atm_ticket_eventcode", nativeQuery = true)
	List<AtmTicketEvent> getAtmTicketEvent(@Param("username") String userId,
			@Param("atm_ticket_eventcode") String atmTicketEventcode);
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
