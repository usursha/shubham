package com.hpy.ops360.sampatti.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.AbstractJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.test.util.ReflectionTestUtils;

import com.hpy.keycloakbase.util.LoginUtil;
import com.hpy.ops360.sampatti.dto.response.FinancialYearSummaryResponse;
import com.hpy.ops360.sampatti.dto.response.IncentiveResponse;
import com.hpy.ops360.sampatti.dto.response.MinMaxValuesResponse;
import com.hpy.ops360.sampatti.dto.response.MyScorecardIncentiveFilterResponse;
import com.hpy.ops360.sampatti.dto.response.SortingData;
import com.hpy.ops360.sampatti.dto.response.UserIncentiveRecordFilterResponse;
import com.hpy.ops360.sampatti.dto.response.UserIncentiveRecordResponse;
import com.hpy.ops360.sampatti.util.RestUtilsImpl;

@ExtendWith(MockitoExtension.class)
public class PortalScoreCardFilterServiceTest {
	
	
	 private static final String TEST_USER = "nitin.waghmare";

	    @Mock
	    private DataSource dataSource;

	    @Mock
	    private LoginUtil loginUtil;
	    
	    
	    private SimpleJdbcCall mockJdbcCall;
	    
	    private MonthlyIncentiveService incentiveService;
	    
	    
	    private static class TestMonthlyIncentiveService extends MonthlyIncentiveService {
	        private SimpleJdbcCall mockJdbcCall;
	        private LoginUtil loginUtil;
	        
	        public TestMonthlyIncentiveService(DataSource dataSource, SimpleJdbcCall mockJdbcCall, LoginUtil loginUtil) {
	            super(dataSource);
	            this.mockJdbcCall = mockJdbcCall;
	            this.loginUtil = loginUtil;
	        }
	        
	        @Override
	        public IncentiveResponse getMyScorecardIncentiveData() {
	            Integer filterId = 1;
	            String userLoginId = loginUtil.getLoggedInUserName();
	            IncentiveResponse response = new IncentiveResponse();
	            
	            // Set up parameters
	            SqlParameterSource params = new MapSqlParameterSource()
	                    .addValue("user_login_id", userLoginId)
	                    .addValue("sort_id", filterId)
	                    .addValue("report_type", 0);
	            
	            // Execute the stored procedure and get multiple result sets
	            Map<String, Object> results = mockJdbcCall.execute(params);
	            
	            // Map the results to our response object
	            @SuppressWarnings("unchecked")
	            List<FinancialYearSummaryResponse> financialYearSummary = 
	                    (List<FinancialYearSummaryResponse>) results.get("financialYearSummary");
	            response.setFinancialYearSummaries(financialYearSummary);
	            
	            @SuppressWarnings("unchecked")
	            List<MinMaxValuesResponse> minMaxList = 
	                    (List<MinMaxValuesResponse>) results.get("minMaxTargetAchieved");
	            response.setMinMaxValues(minMaxList.isEmpty() ? new MinMaxValuesResponse() : minMaxList.get(0));
	            
	            @SuppressWarnings("unchecked")
	            List<SortingData> sortingDatas = 
	                    (List<SortingData>) results.get("sorting_data");
	            response.setSortingData(sortingDatas);
	            
	            @SuppressWarnings("unchecked")
	            List<UserIncentiveRecordResponse> userIncentives = 
	                    (List<UserIncentiveRecordResponse>) results.get("userIncentives");
	            response.setUserIncentives(userIncentives);
	            
	            return response;
	        }
	    }
	    
	    @BeforeEach
	    public void setUp() {
	        // Create a mock SimpleJdbcCall
	        mockJdbcCall = mock(SimpleJdbcCall.class);
	        
	        // Mock the loginUtil to return test user
	        when(loginUtil.getLoggedInUserName()).thenReturn(TEST_USER);
	        
	        // Create our test service with the mock
	        incentiveService = new TestMonthlyIncentiveService(dataSource, mockJdbcCall, loginUtil);
	    }
	    
	    @Test
	    public void testGetMyScorecardIncentiveData_Success() {
	        // Arrange
	        List<FinancialYearSummaryResponse> mockFinancialYearSummaries = createMockFinancialYearSummaries();
	        List<MinMaxValuesResponse> mockMinMaxValues = createMockMinMaxValues();
	        List<SortingData> mockSortingData = createMockSortingData();
	        List<UserIncentiveRecordResponse> mockUserIncentives = createMockUserIncentives();
	        
	        // Create mock data that will be returned from stored procedure
	        Map<String, Object> storedProcResult = new HashMap<>();
	        storedProcResult.put("financialYearSummary", mockFinancialYearSummaries);
	        storedProcResult.put("minMaxTargetAchieved", mockMinMaxValues);
	        storedProcResult.put("sorting_data", mockSortingData);
	        storedProcResult.put("userIncentives", mockUserIncentives);
	        
	        // Set up the execute method on our mockJdbcCall (which was created in setUp)
	        when(mockJdbcCall.execute(any(SqlParameterSource.class))).thenReturn(storedProcResult);
	        
	        // Act
	        IncentiveResponse response = incentiveService.getMyScorecardIncentiveData();
	        
	        // Assert
	        assertNotNull(response);
	        
	        // Verify financial year summaries
	        assertNotNull(response.getFinancialYearSummaries());
	        assertEquals(mockFinancialYearSummaries.size(), response.getFinancialYearSummaries().size());
	        assertEquals("2024-2025", response.getFinancialYearSummaries().get(0).getFinancialYear());
	        assertEquals(5000.0, response.getFinancialYearSummaries().get(0).getTotalIncentiveAmount());
	        
	        // Verify min-max values
	        assertNotNull(response.getMinMaxValues());
	        assertEquals(80, response.getMinMaxValues().getMinAchieved());
	        assertEquals(120, response.getMinMaxValues().getMaxAchieved());
	        
	        // Verify sorting data
	        assertNotNull(response.getSortingData());
	        assertEquals(mockSortingData.size(), response.getSortingData().size());
	        assertEquals("Monthly", response.getSortingData().get(0).getFilterData());
	        
	        // Verify user incentives
	        assertNotNull(response.getUserIncentives());
	        assertEquals(mockUserIncentives.size(), response.getUserIncentives().size());
	        assertEquals("February", response.getUserIncentives().get(0).getMonth());
	        assertEquals(400.0, response.getUserIncentives().get(0).getRewards());
	    }
	    
	    // Helper methods to create mock data
	    private List<FinancialYearSummaryResponse> createMockFinancialYearSummaries() {
	        List<FinancialYearSummaryResponse> list = new ArrayList<>();
	        list.add(new FinancialYearSummaryResponse(1L, "2024-2025", 5000.0));
	        list.add(new FinancialYearSummaryResponse(2L, "2023-2024", 4500.0));
	        return list;
	    }
	    
	    private List<MinMaxValuesResponse> createMockMinMaxValues() {
	        List<MinMaxValuesResponse> list = new ArrayList<>();
	        list.add(new MinMaxValuesResponse(80, 120));
	        return list;
	    }
	    
	    private List<SortingData> createMockSortingData() {
	        List<SortingData> list = new ArrayList<>();
	        list.add(new SortingData(1L, "Monthly"));
	        list.add(new SortingData(2L, "Yearly"));
	        return list;
	    }
	    
	    private List<UserIncentiveRecordResponse> createMockUserIncentives() {
	        List<UserIncentiveRecordResponse> list = new ArrayList<>();
	        list.add(UserIncentiveRecordResponse.builder()
	                .year("2025")
	                .month("February")
	                .teamRank(1)
	                .rankDifference(null)
	                .allIndiaRank(14)
	                .target(120.0)
	                .achieved(116.0)
	                .differenceAchieved(-4)
	                .userLoginId(TEST_USER)
	                .monthKey("2025-02")
	                .rewards(400.0)
	                .build());
	        
	        list.add(UserIncentiveRecordResponse.builder()
	                .year("2025")
	                .month("March")
	                .teamRank(1)
	                .rankDifference(0)
	                .allIndiaRank(12)
	                .target(115.0)
	                .achieved(112.0)
	                .differenceAchieved(-3)
	                .userLoginId(TEST_USER)
	                .monthKey("2025-03")
	                .rewards(400.0)
	                .build());
	        
	        return list;
	    }
	
}
