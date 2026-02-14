package com.hpy.ops360.dashboard.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.CmMtdUptime;

@Repository
public class CmMtdUptimeRepository {// extends JpaRepository<CmMtdUptime, Long> {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("deprecation")
	public CmMtdUptime getUptimeForCM(String cmUserId) {
		return jdbcTemplate.queryForObject("EXEC dbo.Usp_get_Uptime_for_CM ?", new Object[] { cmUserId },
				new UptimeRowMapper());
	}

	private static class UptimeRowMapper implements RowMapper<CmMtdUptime> {
		@Override
		public CmMtdUptime mapRow(ResultSet rs, int rowNum) throws SQLException {
			CmMtdUptime uptime = new CmMtdUptime();
			uptime.setSrNo(rs.getLong("sr_no"));
			uptime.setCmMtdUptime(rs.getString("CM_UPTIME"));
			return uptime;
		}
	}
}
