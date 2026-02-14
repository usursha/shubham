package com.hpy.ops360.sampatti.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;

import com.hpy.keycloakbase.util.LoginUtil;
import com.hpy.ops360.sampatti.dto.AppLeaderBoardDTO;
import com.hpy.ops360.sampatti.dto.AppLeaderBoardRequestDto;
import com.hpy.ops360.sampatti.dto.AppLeaderBoardResponseListDto;
import com.hpy.ops360.sampatti.entity.AppLeaderBoardEntity;
import com.hpy.ops360.sampatti.repository.AppLeaderBoardRepo;
import com.hpy.rest.exception.RequestMismatchExceptionDto;

import jakarta.servlet.http.HttpServletRequest;

@SpringBootTest
@EnableCaching 
public class AppLeaderBoardCachingServiceTest {

    @Mock
    private AppLeaderBoardRepo appLeaderBoardRepo;

    @Mock
    private LoginUtil util;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private AppLeaderBoardCachingService serviceUnderTest;
    
    @Mock
    private HttpServletRequest mockRequest;
    
    @Mock
    private HttpServletRequest httpRequest;  // the HttpServletRequest

    @Mock
    private AppLeaderBoardRequestDto request;
    
	@BeforeEach
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MockitoAnnotations.openMocks(this);
		Field field = serviceUnderTest.getClass().getDeclaredField("profilePictureUrl");
	    field.setAccessible(true);
	    field.set(serviceUnderTest, "https://ops360uat.hitachi-payments.com/ops/gateway/feign-client/get-profile-picture-file/%s"); // Set dummy template

	}

	
	@Test
	void testGetCeLeaderBoardDataList_GroupedByAllIndiaRank() throws Exception {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Rank (High - Low)");
	    request.setMonthYear("May-2025");
	    request.setUserType("CE");
	    request.setSearchKeyword("Anand");
	    request.setZoneList("Zone1");
	    request.setStateList("State1");
	    request.setCityList("City1");
	    request.setPageIndex(1);
	    request.setPageSize(10);

	    List<AppLeaderBoardEntity> mockEntities = List.of(
	        new AppLeaderBoardEntity(1L, 1001L, 1, 1, "Mahesh Patil", "mahesh.patil", 100, 90, 10, "1000", "200", "Gold", "Delhi", "Manager"),
	        new AppLeaderBoardEntity(2L, 1002L, 2, 1, "Rishabh Jain", "rishabh.jain", 110, 100, 10, "1100", "220", "Silver", "Delhi", "Manager"),
	        new AppLeaderBoardEntity(3L, 1003L, 3, 2, "Anand Waghmare", "anand.waghmare", 120, 100, 20, "1200", "250", "Bronze", "Mumbai", "Supervisor")
	    );

	    when(appLeaderBoardRepo.getLeaderboardDataCount(any(), any(), any(), any(), any(), any(), any())).thenReturn(3L);
	    when(appLeaderBoardRepo.getLeaderboardData(any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(mockEntities);

	    AppLeaderBoardResponseListDto result = serviceUnderTest.getCeLeaderBoardDataList(request);
	    assertEquals(3L, result.getCount());

	    // Expect 2 groups: one for rank 1 and one for rank 2
	    List<AppLeaderBoardDTO> records = result.getRecords();
	    assertEquals(2, records.size());

	    // Confirm grouping by AllIndiaRank = 1
	    AppLeaderBoardDTO rank1Group = records.get(0);
	    assertEquals(1, rank1Group.getAllIndiaRank());
	    assertEquals(2, rank1Group.getRankChildren().size());

	    // Confirm grouping by AllIndiaRank = 2
	    AppLeaderBoardDTO rank2Group = records.get(1);
	    assertEquals(2, rank2Group.getAllIndiaRank());
	    assertEquals(1, rank2Group.getRankChildren().size());
	}
	
	@Test
	void testGetCeLeaderBoardDataList_InvalidSortOrder_ShouldThrowException() {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Invalid Order"); // Invalid sort order
	    assertThrows(RequestMismatchExceptionDto.class, () -> {
	        serviceUnderTest.getCeLeaderBoardDataList(request);
	    });
	}
	
	@Test
	void testGetCeLeaderBoardDataList_EmptyData_ReturnsEmptyList() throws Exception {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Rank (High - Low)");
	    request.setMonthYear("May-2025");
	    request.setUserType("CE");
	    request.setSearchKeyword("Anand");
	    request.setZoneList("Zone1");
	    request.setStateList("State1");
	    request.setCityList("City1");
	    request.setPageIndex(1);
	    request.setPageSize(10);

	    // Simulate empty data from repository
	    when(appLeaderBoardRepo.getLeaderboardDataCount(any(), any(), any(), any(), any(), any(), any())).thenReturn(0L);
	    when(appLeaderBoardRepo.getLeaderboardData(any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

	    AppLeaderBoardResponseListDto result = serviceUnderTest.getCeLeaderBoardDataList(request);

	    // Expect count to be 0 and records to be empty
	    assertEquals(0, result.getCount());
	    assertTrue(result.getRecords().isEmpty());
	}
	
	@Test
	void testGetCeLeaderBoardDataList_NullFields_HandledGracefully() throws Exception {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Rank (High - Low)");
	    request.setMonthYear(null); // Null monthYear
	    request.setUserType(null);  // Null userType
	    request.setSearchKeyword(null);  // Null searchKeyword
	    request.setZoneList(null);  // Null zoneList
	    request.setStateList(null);  // Null stateList
	    request.setCityList(null);  // Null cityList
	    request.setPageIndex(1);
	    request.setPageSize(10);

	    // Simulate empty data from repository
	    when(appLeaderBoardRepo.getLeaderboardDataCount(any(), any(), any(), any(), any(), any(), any())).thenReturn(0L);
	    when(appLeaderBoardRepo.getLeaderboardData(any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

	    AppLeaderBoardResponseListDto result = serviceUnderTest.getCeLeaderBoardDataList(request);

	    // Expect count to be 0 and records to be empty, despite null fields
	    assertEquals(0, result.getCount());
	    assertTrue(result.getRecords().isEmpty());
	}
	
	@Test
	void testGetCeLeaderBoardDataList_NoUsersForRank() throws Exception {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Rank (High - Low)");
	    request.setMonthYear("May-2025");
	    request.setUserType("CE");
	    request.setSearchKeyword("Rishabh");
	    request.setZoneList("Zone1");
	    request.setStateList("State1");
	    request.setCityList("City1");
	    request.setPageIndex(1);
	    request.setPageSize(10);

	    List<AppLeaderBoardEntity> mockEntities = List.of(
	        new AppLeaderBoardEntity(1L, 1001L, 1, 1, "Rishabh Jain", "rishabh.jain", 100, 90, 10, "1000", "200", "Gold", "Delhi", "Manager"),
	        new AppLeaderBoardEntity(2L, 1002L, 2, 1, "Mahesh Patil", "mahesh.patil", 110, 100, 10, "1100", "220", "Silver", "Delhi", "Manager")
	    );

	    // Simulate no entities for rank 3
	    when(appLeaderBoardRepo.getLeaderboardDataCount(any(), any(), any(), any(), any(), any(), any())).thenReturn(2L);
	    when(appLeaderBoardRepo.getLeaderboardData(any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(mockEntities);

	    AppLeaderBoardResponseListDto result = serviceUnderTest.getCeLeaderBoardDataList(request);

	    // Expect 2 groups, but no users for rank 3
	    List<AppLeaderBoardDTO> records = result.getRecords();
	    assertEquals(1, records.size());  // Only rank 1 and rank 2 should have users

	    // Ensure there's no group for rank 3
	    boolean rank3Exists = records.stream().anyMatch(group -> group.getAllIndiaRank() == 3);
	    assertFalse(rank3Exists);  // No rank 3 group
	}
	
	@Test
	void testGetCeLeaderBoardDataList_Pagination() throws Exception {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Rank (High - Low)");
	    request.setMonthYear("May-2025");
	    request.setUserType("CE");
	    request.setSearchKeyword("Nitin");
	    request.setZoneList("Zone1");
	    request.setStateList("State1");
	    request.setCityList("City1");
	    request.setPageIndex(1);  // Page 1
	    request.setPageSize(2);   // Page size of 2

	    // Mock data: 5 records in total, we should expect page 1 to return records 3-4
	    List<AppLeaderBoardEntity> mockEntities = List.of(
	        new AppLeaderBoardEntity(1L, 1001L, 1, 1, "Anand Waghmare", "anand.waghmare", 100, 90, 10, "1000", "200", "Gold", "Delhi", "Manager"),
	        new AppLeaderBoardEntity(2L, 1002L, 2, 1, "Rishabh Jain", "rishabh.jain", 110, 100, 10, "1100", "220", "Silver", "Delhi", "Manager"),
	        new AppLeaderBoardEntity(3L, 1003L, 3, 1, "Mahesh Patil", "mahesh.patil", 120, 100, 20, "1200", "250", "Bronze", "Mumbai", "Manager"),
	        new AppLeaderBoardEntity(4L, 1004L, 4, 2, "Satish Chandra", "satish.chandra", 130, 110, 20, "1300", "270", "Platinum", "Mumbai", "Manager"),
	        new AppLeaderBoardEntity(5L, 1005L, 5, 2, "Nitin Waghmare", "nitin.waghmare", 140, 120, 20, "1400", "300", "Diamond", "Delhi", "Manager")
	    );

	    when(appLeaderBoardRepo.getLeaderboardDataCount(any(), any(), any(), any(), any(), any(), any())).thenReturn(5L);
	    when(appLeaderBoardRepo.getLeaderboardData(any(), any(), any(), any(), any(), any(), any(), eq(1), eq(2))).thenReturn(mockEntities.subList(2, 4));  // Return records for page 1
	    AppLeaderBoardResponseListDto result = serviceUnderTest.getCeLeaderBoardDataList(request);
	    assertEquals(5, result.getCount());
	    assertEquals(2, result.getRecords().size());  // Page 1 with 2 records
	    assertEquals("Mahesh Patil", result.getRecords().get(0).getRankChildren().get(0).getFullName());  // First name on page 1
	    assertEquals("Satish Chandra", result.getRecords().get(1).getRankChildren().get(0).getFullName());  // Second name on page 1
	}
	
	@Test
	void testGetCeLeaderBoardDataList_SortingOrderLowToHigh() throws Exception {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Rank (Low - High)");  // Sorting by low to high rank
	    request.setMonthYear("May-2025");
	    request.setUserType("CE");
	    request.setSearchKeyword("Rishabh");
	    request.setZoneList("Zone1");
	    request.setStateList("State1");
	    request.setCityList("City1");
	    request.setPageIndex(1);
	    request.setPageSize(10);

	    // Sample mock data with varying ranks (high to low rank)
	    List<AppLeaderBoardEntity> mockEntities = List.of(
	        new AppLeaderBoardEntity(1L, 1001L, 1, 1, "Mahesh Patil", "mahesh.patil", 100, 90, 10, "1000", "200", "Gold", "Delhi", "Manager"),
	        new AppLeaderBoardEntity(2L, 1002L, 2, 1, "Satish Chandra", "satish.chandra", 110, 100, 10, "1100", "220", "Silver", "Delhi", "Manager"),
	        new AppLeaderBoardEntity(3L, 1003L, 3, 2, "Nitin Waghmare", "nitin.waghmare", 120, 100, 20, "1200", "250", "Bronze", "Mumbai", "Supervisor")
	    );

	    // Mock repository responses
	    when(appLeaderBoardRepo.getLeaderboardDataCount(any(), any(), any(), any(), any(), any(), any())).thenReturn(3L);
	    when(appLeaderBoardRepo.getLeaderboardData(any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(mockEntities);

	    AppLeaderBoardResponseListDto result = serviceUnderTest.getCeLeaderBoardDataList(request);
	    
	    assertEquals(3L, result.getCount());  // Expecting 3 records

	    // Get the sorted records
	    List<AppLeaderBoardDTO> records = result.getRecords();

	    // Expect 2 groups based on AllIndiaRank
	    assertEquals(2, records.size());

	    // Confirm the grouping of records based on AllIndiaRank
	    AppLeaderBoardDTO rank1Group = records.get(0);  // Expect rank 1 to come first (low to high order)
	    assertEquals(1, rank1Group.getAllIndiaRank());
	    assertEquals(2, rank1Group.getRankChildren().size());  // 2 records with rank 1

	    AppLeaderBoardDTO rank2Group = records.get(1);  // Expect rank 2 to come second (low to high order)
	    assertEquals(2, rank2Group.getAllIndiaRank());
	    assertEquals(1, rank2Group.getRankChildren().size());  // 1 record with rank 2
	}
	
	@Test
	void testGetCeLeaderBoardDataList_InvalidSortingOrder_ShouldThrowException() {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Invalid Sort Order");  // Invalid sort order

	    assertThrows(RequestMismatchExceptionDto.class, () -> {
	        serviceUnderTest.getCeLeaderBoardDataList(request);
	    });
	}
	
	@Test
	void testGetCELeaderBoardDataList_EmptyData_ReturnsEmptyList() throws Exception {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Rank (Low - High)");
	    request.setMonthYear("May-2025");
	    request.setUserType("CM");
	    request.setSearchKeyword("Rishabh");
	    request.setZoneList("Zone1");
	    request.setStateList("State1");
	    request.setCityList("City1");
	    request.setPageIndex(1);
	    request.setPageSize(10);

	    // Simulate empty data from repository
	    when(appLeaderBoardRepo.getLeaderboardDataCount(any(), any(), any(), any(), any(), any(), any())).thenReturn(0L);
	    when(appLeaderBoardRepo.getLeaderboardData(any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

	    AppLeaderBoardResponseListDto result = serviceUnderTest.getCeLeaderBoardDataList(request);

	    // Expect count to be 0 and records to be empty
	    assertEquals(0, result.getCount());
	    assertTrue(result.getRecords().isEmpty());
	}

	@Test
	void testGetCELeaderBoardDataList_NullFields_HandledGracefully() throws Exception {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Rank (Low - High)");
	    request.setMonthYear(null); // Null monthYear
	    request.setUserType(null);  // Null userType
	    request.setSearchKeyword(null);  // Null searchKeyword
	    request.setZoneList(null);  // Null zoneList
	    request.setStateList(null);  // Null stateList
	    request.setCityList(null);  // Null cityList
	    request.setPageIndex(1);
	    request.setPageSize(10);

	    // Simulate data with null fields
	    List<AppLeaderBoardEntity> mockEntities = List.of(
	        new AppLeaderBoardEntity(1L, 1001L, 1, 1, "Nitin Waghmare", "nitin.waghmare", 100, 90, 10, "1000", "200", "Gold", "Delhi", "Manager")
	    );

	    // Mock repository response
	    when(appLeaderBoardRepo.getLeaderboardDataCount(any(), any(), any(), any(), any(), any(), any())).thenReturn(1L);
	    when(appLeaderBoardRepo.getLeaderboardData(any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(mockEntities);

	    AppLeaderBoardResponseListDto result = serviceUnderTest.getCeLeaderBoardDataList(request);

	    // Verify data is returned correctly despite null fields
	    assertEquals(1, result.getCount());
	    assertFalse(result.getRecords().isEmpty());
	    assertEquals("Nitin Waghmare", result.getRecords().get(0).getRankChildren().get(0).getFullName());
	}

	@Test
	void testGetCeLeaderBoardDataList_MissingDataForSpecificRank() throws Exception {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Rank (Low - High)");
	    request.setMonthYear("May-2025");
	    request.setUserType("CE");
	    request.setSearchKeyword("Mahesh");
	    request.setZoneList("Zone1");
	    request.setStateList("State1");
	    request.setCityList("City1");
	    request.setPageIndex(1);
	    request.setPageSize(10);

	    List<AppLeaderBoardEntity> mockEntities = List.of(
	        new AppLeaderBoardEntity(1L, 1001L, 1, 1, "Nitin Waghmare", "nitin.waghmare", 100, 90, 10, "1000", "200", "Gold", "Delhi", "Manager"),
	        new AppLeaderBoardEntity(2L, 1002L, 2, 1, "Mahesh Patil", "mahesh.patil", 110, 100, 10, "1100", "220", "Silver", "Delhi", "Manager")
	    );

	    // Simulate no records for rank 3
	    when(appLeaderBoardRepo.getLeaderboardDataCount(any(), any(), any(), any(), any(), any(), any())).thenReturn(3L);
	    when(appLeaderBoardRepo.getLeaderboardData(any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(mockEntities);

	    AppLeaderBoardResponseListDto result = serviceUnderTest.getCeLeaderBoardDataList(request);

	    // Verify that rank 3 group does not exist
	    List<AppLeaderBoardDTO> records = result.getRecords();
	    assertEquals(1, records.size());  // Only rank 1 and rank 2 should exist, no rank 3

	    boolean rank3Exists = records.stream().anyMatch(group -> group.getAllIndiaRank() == 3);
	    assertFalse(rank3Exists);  // Ensure no rank 3 group exists
	}
	
	@Test
	void testGetCeLeaderBoardDataList_PaginationNoData() throws Exception {
	    AppLeaderBoardRequestDto request = new AppLeaderBoardRequestDto();
	    request.setSortOrder("Rank (Low - High)");
	    request.setMonthYear("May-2025");
	    request.setUserType("CM");
	    request.setSearchKeyword("Rajat");
	    request.setZoneList("Zone1");
	    request.setStateList("State1");
	    request.setCityList("City1");
	    request.setPageIndex(10);  // High page index with no data
	    request.setPageSize(10);   // Page size 10

	    // Simulate no data for pagination
	    when(appLeaderBoardRepo.getLeaderboardDataCount(any(), any(), any(), any(), any(), any(), any())).thenReturn(0L);
	    when(appLeaderBoardRepo.getLeaderboardData(any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

	    AppLeaderBoardResponseListDto result = serviceUnderTest.getCeLeaderBoardDataList(request);

	    // Expect count to be 0 and records to be empty for pagination with no data
	    assertEquals(0, result.getCount());
	    assertTrue(result.getRecords().isEmpty());
	}

}
