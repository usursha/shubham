package com.hpy.ops360.sampatti.service;

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
public class PortalScoreCardServiceTest {

//	@InjectMocks
//	private MonthlyIncentiveService incentiveService;

	@Mock
	private DataSource dataSource;

	@Mock
	private RestUtilsImpl util;

	@Mock
	private SimpleJdbcCall simpleJdbcCall;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private LoginUtil loginUtil;

	private String mockUsername;

	private static final String TEST_USER = "nitin.waghmare";
	private static final Integer TEST_YEAR_ID = 2025;
	private static final Integer TEST_MIN_ACHIEVED = 80;
	private static final Integer TEST_MAX_ACHIEVED = 100;
	private static final Integer TEST_SORT_ID = 1;

	/*
	 * 1)Success scenario
	 * 2)Empty result scenario 
	 * 3)Null parameters 
	 * 4)SQLException handling 
	 */

	private static class TestMonthlyIncentiveService extends MonthlyIncentiveService {
		private SimpleJdbcCall mockJdbcCall;
		private LoginUtil loginUtil;

		public TestMonthlyIncentiveService(DataSource dataSource, SimpleJdbcCall mockJdbcCall, LoginUtil loginUtil) {
			super(dataSource);
			this.mockJdbcCall = mockJdbcCall;
			this.loginUtil = loginUtil;
		}

		@Override
		protected SimpleJdbcCall createJdbcCall() {
			return mockJdbcCall;
		}

		// This is the key override to prevent the NullPointerException
		@Override
		public MyScorecardIncentiveFilterResponse getUserIncentivesByFilters(Integer yearId, Integer minAchieved,
				Integer maxAchieved, Integer sortId) {

			String userLoginId = loginUtil.getLoggedInUserName();

			// Set up parameters
			SqlParameterSource params = new MapSqlParameterSource().addValue("user_login_id", userLoginId)
					.addValue("year_id", yearId).addValue("min_achieved", minAchieved)
					.addValue("max_achieved", maxAchieved).addValue("sort_id", sortId);

			// Execute the stored procedure and get the result using our mock
			Map<String, Object> results = mockJdbcCall.execute(params);

			// Extract the incentives from the result
			@SuppressWarnings("unchecked")
			List<UserIncentiveRecordFilterResponse> userIncentives = (List<UserIncentiveRecordFilterResponse>) results
					.get("userIncentives");

			// Create and return the response
			MyScorecardIncentiveFilterResponse response = new MyScorecardIncentiveFilterResponse(userIncentives);
			return response;
		}
	}

	private TestMonthlyIncentiveService incentiveService;
	private SimpleJdbcCall mockJdbcCall;

	@BeforeEach
	public void setUp() {
		// Create a mock SimpleJdbcCall
		mockJdbcCall = mock(SimpleJdbcCall.class);

		// Mock the LoginUtil to return test user
		when(loginUtil.getLoggedInUserName()).thenReturn(TEST_USER);

		// Create our test service with the mock and test user
		incentiveService = new TestMonthlyIncentiveService(dataSource, mockJdbcCall, loginUtil);
	}

	@Test
	public void testGetUserIncentivesByFilters_Success() {
		// Arrange
		List<UserIncentiveRecordFilterResponse> mockData = createMockIncentivesList();

		// Create mock data that will be returned from stored procedure
		Map<String, Object> storedProcResult = new HashMap<>();
		storedProcResult.put("userIncentives", mockData);

		// Set up the execute method on our mockJdbcCall (which was created in setUp)
		when(mockJdbcCall.execute(any(SqlParameterSource.class))).thenReturn(storedProcResult);

		// Act
		MyScorecardIncentiveFilterResponse response = incentiveService.getUserIncentivesByFilters(TEST_YEAR_ID,
				TEST_MIN_ACHIEVED, TEST_MAX_ACHIEVED, TEST_SORT_ID);

		// Assert
		assertNotNull(response);
		assertNotNull(response.getUserIncentives());
		assertEquals(mockData.size(), response.getUserIncentives().size());

		UserIncentiveRecordFilterResponse firstRecord = response.getUserIncentives().get(0);
		assertEquals("2025", firstRecord.getYear());
		assertEquals("February", firstRecord.getMonth());
		assertEquals(TEST_USER, firstRecord.getUserLoginId());
		assertEquals(400.0, firstRecord.getIncentiveAmount());
	}

	// Helper method to create mock incentives list
	private List<UserIncentiveRecordFilterResponse> createMockIncentivesList() {
		List<UserIncentiveRecordFilterResponse> incentives = new ArrayList<>();

		UserIncentiveRecordFilterResponse incentive1 = new UserIncentiveRecordFilterResponse();
		incentive1.setYear("2025");
		incentive1.setMonth("February");
		incentive1.setTeamRank(1);
		incentive1.setRankDifference(null);
		incentive1.setAllIndiaRank(14);
		incentive1.setTarget(120);
		incentive1.setAchieved(116);
		incentive1.setDiffAchievedTarget(-4);
		incentive1.setUserLoginId("nitin.waghmare");
		incentive1.setMonthKey("2025-02");
		incentive1.setIncentiveAmount(400.0);
		incentive1.setFinancialYear("2024-2025");

		UserIncentiveRecordFilterResponse incentive2 = new UserIncentiveRecordFilterResponse();
		incentive2.setYear("2025");
		incentive2.setMonth("March");
		incentive2.setTeamRank(1);
		incentive2.setRankDifference(0);
		incentive2.setAllIndiaRank(12);
		incentive2.setTarget(115);
		incentive2.setAchieved(112);
		incentive2.setDiffAchievedTarget(-3);
		incentive2.setUserLoginId("nitin.waghmare");
		incentive2.setMonthKey("2025-03");
		incentive2.setIncentiveAmount(400.0);
		incentive2.setFinancialYear("2024-2025");

		incentives.add(incentive1);
		incentives.add(incentive2);

		return incentives;
	}

	@Test
	public void testGetUserIncentivesByFilters_EmptyResult() {
		// Arrange - Create an empty list of incentives
		List<UserIncentiveRecordFilterResponse> emptyList = new ArrayList<>();
		Map<String, Object> storedProcResult = new HashMap<>();
		storedProcResult.put("userIncentives", emptyList);
		when(mockJdbcCall.execute(any(SqlParameterSource.class))).thenReturn(storedProcResult);

		// Act
		MyScorecardIncentiveFilterResponse response = incentiveService.getUserIncentivesByFilters(TEST_YEAR_ID,
				TEST_MIN_ACHIEVED, TEST_MAX_ACHIEVED, TEST_SORT_ID);

		// Assert
		assertNotNull(response);
		assertNotNull(response.getUserIncentives());
		assertEquals(0, response.getUserIncentives().size());
	}

	@Test
	public void testGetUserIncentivesByFilters_NullParameters() {
		// Arrange
		List<UserIncentiveRecordFilterResponse> mockData = createMockIncentivesList();
		Map<String, Object> storedProcResult = new HashMap<>();
		storedProcResult.put("userIncentives", mockData);

		// Capture parameters to verify null handling
		ArgumentCaptor<SqlParameterSource> paramCaptor = ArgumentCaptor.forClass(SqlParameterSource.class);
		when(mockJdbcCall.execute(paramCaptor.capture())).thenReturn(storedProcResult);

		// Act - Call with null parameters
		MyScorecardIncentiveFilterResponse response = incentiveService.getUserIncentivesByFilters(null, null, null,
				null);

		// Assert
		assertNotNull(response);
		assertNotNull(response.getUserIncentives());

		// Verify parameters were passed as null
		SqlParameterSource capturedParams = paramCaptor.getValue();
		assertNull(capturedParams.getValue("year_id"));
		assertNull(capturedParams.getValue("min_achieved"));
		assertNull(capturedParams.getValue("max_achieved"));
		assertNull(capturedParams.getValue("sort_id"));
	}

	@Test
	public void testGetUserIncentivesByFilters_SQLException() {
		// Arrange - Setup the mock to throw SQLException
		when(mockJdbcCall.execute(any(SqlParameterSource.class))).thenThrow(new UncategorizedSQLException("SQL Error",
				"Error executing stored procedure", new SQLException("Database connection error")));

		// Act & Assert
		Exception exception = assertThrows(UncategorizedSQLException.class, () -> {
			incentiveService.getUserIncentivesByFilters(TEST_YEAR_ID, TEST_MIN_ACHIEVED, TEST_MAX_ACHIEVED,
					TEST_SORT_ID);
		});

		// Verify exception details
		assertTrue(exception.getMessage().contains("SQL Error"));
		assertTrue(((UncategorizedSQLException) exception).getSQLException().getMessage()
				.contains("Database connection error"));
	}

}
