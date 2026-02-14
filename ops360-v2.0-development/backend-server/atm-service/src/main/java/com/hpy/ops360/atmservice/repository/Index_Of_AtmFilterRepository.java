package com.hpy.ops360.atmservice.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hpy.ops360.atmservice.dto.ATMStatusFilterResponseDTO;
import com.hpy.ops360.atmservice.dto.BankFilterResponseDTO;
import com.hpy.ops360.atmservice.dto.GradeFilterResponseDTO;
import com.hpy.ops360.atmservice.dto.IndexOfAtmFilterResponseDto;
import com.hpy.ops360.atmservice.dto.SortFilterResponseDto;
import com.hpy.ops360.atmservice.dto.UptimeStatusFilterResponseDTO;

@Repository
public class Index_Of_AtmFilterRepository {

	@Autowired
    private JdbcTemplate jdbcTemplate;
    
    public IndexOfAtmFilterResponseDto getAllFilterCounts(String userId) {
    	IndexOfAtmFilterResponseDto response = new IndexOfAtmFilterResponseDto();
        
        // Call stored procedure
        String sql = "{call USP_GetAllFiltersCount_Index_of_ATM(?)}";
        
        jdbcTemplate.execute((Connection connection) -> {
            try (CallableStatement callableStatement = connection.prepareCall(sql)) {
                callableStatement.setString(1, userId);
                
                boolean hasResultSet = callableStatement.execute();
                int resultSetCount = 0;
                
                while (hasResultSet || callableStatement.getUpdateCount() != -1) {
                    if (hasResultSet) {
                        try (ResultSet rs = callableStatement.getResultSet()) {
                            switch (resultSetCount) {
                                case 0: // Sort Options
                                    response.setSortIndexOfAtmFilters(mapSortOptions(rs));
                                    break;
                                case 1: // Bank Counts
                                    response.setBankIndexOfAtmFilters(mapBankCounts(rs));
                                    break;
                                case 2: // Grade Counts
                                    response.setGradeIndexOfAtmFilters(mapGradeCounts(rs));
                                    break;
                                case 3: // Status Counts
                                    response.setAtmStatusIndexOfAtmFilters(mapStatusCounts(rs));
                                    break;
                                case 4: // Uptime Status Counts
                                    response.setUptimeStatusIndexOfAtmFilter(mapUptimeStatusCounts(rs));
                                    break;
                            }
                        }
                        resultSetCount++;
                    }
                    hasResultSet = callableStatement.getMoreResults();
                }
                
                return response;
            }
        });
        
        return response;
    }
    
    private List<SortFilterResponseDto> mapSortOptions(ResultSet rs) throws SQLException {
        List<SortFilterResponseDto> sortOptions = new ArrayList<>();
        while (rs.next()) {
            sortOptions.add(new SortFilterResponseDto(
                rs.getInt("sort_id"),
                rs.getString("sort_name")
            ));
        }
        return sortOptions;
    }
    
    private List<BankFilterResponseDTO> mapBankCounts(ResultSet rs) throws SQLException {
        List<BankFilterResponseDTO> bankCounts = new ArrayList<>();
        while (rs.next()) {
            bankCounts.add(new BankFilterResponseDTO(
                rs.getString("Bank"),
                rs.getInt("ATM_Count")
            ));
        }
        return bankCounts;
    }
    
    private List<GradeFilterResponseDTO> mapGradeCounts(ResultSet rs) throws SQLException {
        List<GradeFilterResponseDTO> gradeCounts = new ArrayList<>();
        while (rs.next()) {
            gradeCounts.add(new GradeFilterResponseDTO(
                rs.getInt("sr_no"),
                rs.getString("Grade"),
                rs.getInt("atm_count")
            ));
        }
        return gradeCounts;
    }
    
    private List<ATMStatusFilterResponseDTO> mapStatusCounts(ResultSet rs) throws SQLException {
        List<ATMStatusFilterResponseDTO> statusCounts = new ArrayList<>();
        while (rs.next()) {
            statusCounts.add(new ATMStatusFilterResponseDTO(
                rs.getString("machine_status"),
                rs.getInt("count")
            ));
        }
        return statusCounts;
    }
    
    private List<UptimeStatusFilterResponseDTO> mapUptimeStatusCounts(ResultSet rs) throws SQLException {
        List<UptimeStatusFilterResponseDTO> uptimeStatusCounts = new ArrayList<>();
        while (rs.next()) {
            uptimeStatusCounts.add(new UptimeStatusFilterResponseDTO(
                rs.getInt("sr_no"),
                rs.getString("uptime_status"),
                rs.getInt("count")
            ));
        }
        return uptimeStatusCounts;
    }
	
}
