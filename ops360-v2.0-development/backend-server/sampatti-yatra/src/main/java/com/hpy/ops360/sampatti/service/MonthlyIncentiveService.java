package com.hpy.ops360.sampatti.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.hpy.keycloakbase.util.LoginUtil;
import com.hpy.ops360.sampatti.dto.request.MyScorecardIncentiveFilterRequest;
import com.hpy.ops360.sampatti.dto.response.FinancialYearSummaryResponse;
import com.hpy.ops360.sampatti.dto.response.IncentiveResponse;
import com.hpy.ops360.sampatti.dto.response.MinMaxValuesResponse;
import com.hpy.ops360.sampatti.dto.response.MyScorecardIncentiveFilterResponse;
import com.hpy.ops360.sampatti.dto.response.SortingData;
import com.hpy.ops360.sampatti.dto.response.UserIncentiveRecordFilterResponse;
import com.hpy.ops360.sampatti.dto.response.UserIncentiveRecordResponse;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyIncentiveService {


	@Autowired
	private LoginUtil util;

	@Autowired
	private DataSource dataSource;
	
	 public MonthlyIncentiveService(DataSource dataSource) {
	        this.dataSource = dataSource;
	    }
	
	public IncentiveResponse getMyScorecardIncentiveData() {
		
		Integer filterId=1;
		String userLoginId = util.getLoggedInUserName();
		IncentiveResponse response = new IncentiveResponse();

				
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
			    .withProcedureName("sp_GetIncentiveByMonthforCm")
			    .withoutProcedureColumnMetaDataAccess()
			    .declareParameters(
			        new SqlParameter("user_login_id", Types.VARCHAR),
			        new SqlParameter("sort_id", Types.INTEGER),
			        new SqlParameter("report_type", Types.INTEGER)
			    )
				.returningResultSet("financialYearSummary", (rs, rowNum) -> {
					FinancialYearSummaryResponse summary = new FinancialYearSummaryResponse();
					summary.setYearId(rs.getLong("year_id"));
					summary.setFinancialYear(rs.getString("financial_year"));
					summary.setTotalIncentiveAmount(rs.getDouble("total_incentive_amount"));
					return summary;
				}).returningResultSet("minMaxTargetAchieved", (rs, rowNum) -> {
					MinMaxValuesResponse minMax = new MinMaxValuesResponse();
					minMax.setMinAchieved(rs.getInt("min_achieved"));
					minMax.setMaxAchieved(rs.getInt("max_achieved"));
					return minMax;
				}).returningResultSet("sorting_data", (rs, rowNum) -> {
					SortingData sortingData = new SortingData();
					sortingData.setSortId(rs.getLong("sort_id"));
					sortingData.setFilterData(rs.getString("filter_data"));
					return sortingData;
				}).returningResultSet("userIncentives", (rs, rowNum) -> {
	                UserIncentiveRecordResponse incentive = new UserIncentiveRecordResponse();
	                
	                try { incentive.setYear(rs.getString("year")); } catch (Exception e) {}
	                try { incentive.setMonth(rs.getString("month")); } catch (Exception e) {}
	                
	                try { incentive.setTeamRank(rs.getInt("scm_rank_waise")); } catch (Exception e) {}
	                
	                try { 
	                    // Proper null handling for rank difference
	                    Integer rankDiff = rs.getObject("scm_rank_difference") != null ? 
	                            rs.getInt("scm_rank_difference") : null;
	                    incentive.setRankDifference(rankDiff);
	                } catch (Exception e) {
	                    incentive.setRankDifference(null);
	                }
	                try { incentive.setAllIndiaRank(rs.getInt("all_india_rank")); } catch (Exception e) {}
	                try { incentive.setTarget(rs.getDouble("target")); } catch (Exception e) {}
	                try { incentive.setAchieved(rs.getDouble("achieved")); } catch (Exception e) {}
	                try { incentive.setDifferenceAchieved(rs.getInt("diff_achieved_target")); } catch (Exception e) {}
	                
	                try { incentive.setUserLoginId(rs.getString("user_login_id")); } catch (Exception e) {}
	                try { incentive.setMonthKey(rs.getString("month_key")); } catch (Exception e) {}
	                
	                try { 
	                    incentive.setRewards(rs.getDouble("incentive_amount")); 
	                } catch (SQLException e) { 
	                    log.debug("Error setting incentive_amount", e); 
	                }
	                
	                return incentive;
	            });

		// Set up parameters
		SqlParameterSource params = new MapSqlParameterSource().addValue("user_login_id", userLoginId)
				.addValue("sort_id", filterId).addValue("report_type", 0); 

		// Execute the stored procedure and get multiple result sets
		Map<String, Object> results = jdbcCall.execute(params);

		// Map the results to our response object
		@SuppressWarnings("unchecked")
		List<FinancialYearSummaryResponse> financialYearSummary = (List<FinancialYearSummaryResponse>) results
				.get("financialYearSummary");
		response.setFinancialYearSummaries(financialYearSummary);

		@SuppressWarnings("unchecked")
		List<MinMaxValuesResponse> minMaxList = (List<MinMaxValuesResponse>) results.get("minMaxTargetAchieved");
		response.setMinMaxValues(minMaxList.isEmpty() ? new MinMaxValuesResponse() : minMaxList.get(0));
		
		@SuppressWarnings("unchecked")
		List<SortingData> sortingDatas = (List<SortingData>) results.get("sorting_data");
		response.setSortingData(sortingDatas);

		@SuppressWarnings("unchecked")
		List<UserIncentiveRecordResponse> userIncentives = (List<UserIncentiveRecordResponse>) results
				.get("userIncentives");
		response.setUserIncentives(userIncentives);

		return response;
	}
	

	public MyScorecardIncentiveFilterResponse getUserIncentivesByFilters(Integer yearId, Integer minAchieved,
            Integer maxAchieved, Integer sortId) {

        String userLoginId = util.getLoggedInUserName();
        log.info("Fetching incentives for user: {} with filters - yearId: {}, minAchieved: {}, maxAchieved: {}, sortId: {}",
                userLoginId, yearId, minAchieved, maxAchieved, sortId);

        SimpleJdbcCall jdbcCall = createJdbcCall();

        // Set up parameters
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("user_login_id", userLoginId)
                .addValue("year_id", yearId)
                .addValue("min_achieved", minAchieved)
                .addValue("max_achieved", maxAchieved)
                .addValue("sort_id", sortId);

        // Execute the stored procedure and get the result
        Map<String, Object> results = jdbcCall.execute(params);

        // Extract the incentives from the result
        @SuppressWarnings("unchecked")
        List<UserIncentiveRecordFilterResponse> userIncentives = (List<UserIncentiveRecordFilterResponse>) results.get("userIncentives");

        // Create and return the response
        MyScorecardIncentiveFilterResponse response = new MyScorecardIncentiveFilterResponse(userIncentives);
        return response;
    }
    
    protected SimpleJdbcCall createJdbcCall() {
        return new SimpleJdbcCall(dataSource)
                .withProcedureName("sp_GetUserIncentives")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("user_login_id", Types.VARCHAR),
                        new SqlParameter("year_id", Types.INTEGER),
                        new SqlParameter("min_achieved", Types.INTEGER),
                        new SqlParameter("max_achieved", Types.INTEGER),
                        new SqlParameter("sort_id", Types.INTEGER)
                )
                .returningResultSet("userIncentives", new RowMapper<UserIncentiveRecordFilterResponse>() {
                    @Override
                    public UserIncentiveRecordFilterResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
                        UserIncentiveRecordFilterResponse incentive = new UserIncentiveRecordFilterResponse();

                        try { incentive.setYear(rs.getString("year")); } catch (SQLException e) { log.debug("Error setting year", e); }
                        try { incentive.setMonth(rs.getString("month")); } catch (SQLException e) { log.debug("Error setting month", e); }
                        try { incentive.setTeamRank(rs.getInt("scm_rank_waise")); } catch (SQLException e) { log.debug("Error setting scm_rank_waise", e); }
                        try { incentive.setRankDifference(rs.getInt("scm_rank_difference")); } catch (SQLException e) { log.debug("Error setting scm_rank_difference", e); }
                        try { incentive.setAllIndiaRank(rs.getInt("all_india_rank")); } catch (SQLException e) { log.debug("Error setting all_india_rank", e); }
                        try { incentive.setTarget(rs.getInt("target")); } catch (SQLException e) { log.debug("Error setting target", e); }
                        try { incentive.setAchieved(rs.getInt("achieved")); } catch (SQLException e) { log.debug("Error setting achieved", e); }
                        try { incentive.setDiffAchievedTarget(rs.getInt("diff_achieved_target")); } catch (SQLException e) { log.debug("Error setting diff_achieved_target", e); }
                        try { incentive.setUserLoginId(rs.getString("user_login_id")); } catch (SQLException e) { log.debug("Error setting user_login_id", e); }
                        try { incentive.setMonthKey(rs.getString("month_key")); } catch (SQLException e) { log.debug("Error setting month_key", e); }
                        try { incentive.setIncentiveAmount(rs.getDouble("incentive_amount")); } catch (SQLException e) { log.debug("Error setting incentive_amount", e); }
                        try { incentive.setFinancialYear(rs.getString("financial_year")); } catch (SQLException e) { log.debug("Error setting financial_year", e); }

                        return incentive;
                    }
                });
    }
}
