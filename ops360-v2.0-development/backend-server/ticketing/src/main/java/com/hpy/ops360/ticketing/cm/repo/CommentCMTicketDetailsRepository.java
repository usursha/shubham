package com.hpy.ops360.ticketing.cm.repo;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.stereotype.Repository;

@Repository
public class CommentCMTicketDetailsRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void insertUpdateCMTicketDetails(String atmId, String ticketNumber, String createBy, String cmComments) {
		new InsertUpdateCMTicketDetailsStoredProcedure(jdbcTemplate).execute(atmId, ticketNumber, createBy, cmComments);
	}

	private static class InsertUpdateCMTicketDetailsStoredProcedure extends StoredProcedure {
		private static final String SP_NAME = "USP_InsertUpdateCMTicketDetails";

		public InsertUpdateCMTicketDetailsStoredProcedure(JdbcTemplate jdbcTemplate) {
			super(jdbcTemplate, SP_NAME);
			declareParameter(new SqlParameter("atmid", Types.VARCHAR));
			declareParameter(new SqlParameter("ticket_number", Types.VARCHAR));
			declareParameter(new SqlParameter("create_by", Types.VARCHAR));
			declareParameter(new SqlParameter("cm_comments", Types.VARCHAR));
			compile();
		}

		public void execute(String atmId, String ticketNumber, String createBy, String cmComments) {
			Map<String, Object> params = new HashMap<>();
			params.put("atmid", atmId);
			params.put("ticket_number", ticketNumber);
			params.put("create_by", createBy);
			params.put("cm_comments", cmComments);
			execute(params);
		}
	}
}
