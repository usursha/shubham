package com.hpy.ops360.ticketing.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.ticketing.dto.ReportDto;
import com.hpy.ops360.ticketing.dto.SummaryReportDto;

import java.util.List;

@Repository
public class SummaryReportRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    
    @SuppressWarnings("deprecation")
	public List<SummaryReportDto> getReportData(String username, String reportUser, String startDate, String endDate) {
        String sql = "EXEC dbo.Usp_get_report_CE_summary ?, ?, ?, ?";
        return jdbcTemplate.query(sql, new Object[]{username, reportUser, startDate, endDate},
                new BeanPropertyRowMapper<>(SummaryReportDto.class));
    }
}
