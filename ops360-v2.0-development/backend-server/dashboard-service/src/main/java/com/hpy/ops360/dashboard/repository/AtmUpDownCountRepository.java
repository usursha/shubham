package com.hpy.ops360.dashboard.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.dashboard.entity.AtmUpDownCount;

@Repository
public class AtmUpDownCountRepository {// extends JpaRepository<AtmUpDownCount, Long> {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("deprecation")
	public AtmUpDownCount getATMCountForCM(String cmUserId) {
		return jdbcTemplate.queryForObject("EXEC dbo.Usp_get_ATM_Count_for_CM ?", new Object[] { cmUserId },
				new ATMCountRowMapper());
	}

	private static class ATMCountRowMapper implements RowMapper<AtmUpDownCount> {
		@Override
		public AtmUpDownCount mapRow(ResultSet rs, int rowNum) throws SQLException {
			AtmUpDownCount atmCount = new AtmUpDownCount();
			atmCount.setSrNo(rs.getLong("sr_no"));
			atmCount.setDownAtm(rs.getInt("DOWN_ATM"));
			atmCount.setUpAtm(rs.getInt("UP_ATM"));
			atmCount.setTotalAtm(rs.getInt("TOTAL_ATM"));
			return atmCount;
		}
	}
}
