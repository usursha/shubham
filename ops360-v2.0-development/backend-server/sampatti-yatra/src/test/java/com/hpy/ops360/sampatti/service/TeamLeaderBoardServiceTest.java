package com.hpy.ops360.sampatti.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hpy.ops360.sampatti.dto.request.TeamLeaderboardDetailsReqDto;
import com.hpy.ops360.sampatti.dto.response.TeamLeaderBoardListDataDto;
import com.hpy.ops360.sampatti.dto.response.TeamLeaderboardDetailsDto;
import com.hpy.ops360.sampatti.dto.response.TeamLeaderboardFilterDataDto;
import com.hpy.ops360.sampatti.entity.CeAgainstCmEntity;
import com.hpy.ops360.sampatti.entity.SortFilterData;
import com.hpy.ops360.sampatti.entity.TargetRange;
import com.hpy.ops360.sampatti.entity.TeamLeaderboardRankDetails;
import com.hpy.ops360.sampatti.entity.UserIncentiveMonthYearEntity;
import com.hpy.ops360.sampatti.repository.AssetRepo;
import com.hpy.ops360.sampatti.repository.CeAgainstCmEntityRepository;
import com.hpy.ops360.sampatti.repository.SortFilterDataRepostiory;
import com.hpy.ops360.sampatti.repository.TargetRangeRepository;
import com.hpy.ops360.sampatti.repository.TeamLeaderboardRankDetailsRepostiory;
import com.hpy.ops360.sampatti.repository.UserIncentiveMonthYearRepository;

@ExtendWith(MockitoExtension.class)
class TeamLeaderBoardServiceTest {
	
	    @Mock
	    private UserIncentiveMonthYearRepository repository;

	    @Mock
	    private CeAgainstCmEntityRepository ceAgainstCmEntityRepository;
	    
	    @Mock
	    private SortFilterDataRepostiory sortFilterDataRepostiory;

	    @Mock
	    private TargetRangeRepository targetRangeRepository;
	    
	    @Mock
	    private TeamLeaderboardRankDetailsRepostiory teamLeaderboardRankDetailsRepostiory;
	    
	    @Mock
	    private AssetRepo assetRepo;

	    @InjectMocks
	    private TeamLeaderBoardService teamLeaderBoardService;
	    
	    @BeforeEach
	    void setUp() throws Exception {
	        MockitoAnnotations.openMocks(this);

	        Field field = TeamLeaderBoardService.class.getDeclaredField("profilePictureUrl");
	        field.setAccessible(true);
	        field.set(teamLeaderBoardService, "https://ops360dev.hitachi-payments.com/ops/gateway/feign-client/get-profile-picture-file/%s");
	    }

	    
	    @Test
	    void testGetTeamLeaderBoardListData() {
	           // Arrange
	           String cmUsername = "nitin.waghmare";
	           // Mock the repository method calls
	           when(repository.getUserIncentivesMonthYear()).thenReturn(
	               List.of(new UserIncentiveMonthYearEntity(1L, "Jan 2025"))
	           );

	           when(ceAgainstCmEntityRepository.getCeAgainstCmList(cmUsername)).thenReturn(
	               List.of(new CeAgainstCmEntity(1L, "ceUser1","ce User1"), new CeAgainstCmEntity(2L, "ceUser2", "ce User2"))
	           );

	            // Act
               TeamLeaderBoardListDataDto result = teamLeaderBoardService.getTeamLeaderBoardListData(cmUsername);

	            // Assert
	           assertNotNull(result);
	           assertEquals(1, result.getMonthYearList().size());
	           assertEquals("Jan 2025", result.getMonthYearList().get(0).getMonthYear());
	           assertEquals(2, result.getCeList().size());
	           assertEquals("ceUser1", result.getCeList().get(0).getCeUserId());
	           assertEquals("ceUser2", result.getCeList().get(1).getCeUserId());

	           // Verify method calls
	           verify(repository, times(1)).getUserIncentivesMonthYear();
	           verify(ceAgainstCmEntityRepository, times(1)).getCeAgainstCmList(cmUsername);
	       }

	    
	    @Test
	    void testGetTeamLeaderBoardListDataWithEmptyData() {
	        // Arrange
	        String cmUsername = "testUser";

	        // Mock the repository method calls to return empty lists
	        when(repository.getUserIncentivesMonthYear()).thenReturn(Collections.emptyList());
	        when(ceAgainstCmEntityRepository.getCeAgainstCmList(cmUsername)).thenReturn(Collections.emptyList());

	        // Act
	        TeamLeaderBoardListDataDto result = teamLeaderBoardService.getTeamLeaderBoardListData(cmUsername);

	        // Assert
	        assertNotNull(result);
	        assertTrue(result.getMonthYearList().isEmpty());
	        assertTrue(result.getCeList().isEmpty());

	        // Verify method calls
	        verify(repository, times(1)).getUserIncentivesMonthYear();
	        verify(ceAgainstCmEntityRepository, times(1)).getCeAgainstCmList(cmUsername);
	    }

	    @Test
	    void testGetTeamLeaderBoardListDataWithNullData() {
	        // Arrange
	        String cmUsername = "nitin.waghmare";

	        // Mock the repository method calls to return null (simulate error in fetching data)
	        when(repository.getUserIncentivesMonthYear()).thenReturn(null);
	        when(ceAgainstCmEntityRepository.getCeAgainstCmList(cmUsername)).thenReturn(null);

	        // Act
	        TeamLeaderBoardListDataDto result = teamLeaderBoardService.getTeamLeaderBoardListData(cmUsername);

	        // Assert
	        assertNotNull(result);
	        assertNull(result.getMonthYearList());
	        assertNull(result.getCeList());

	        // Verify method calls
	        verify(repository, times(1)).getUserIncentivesMonthYear();
	        verify(ceAgainstCmEntityRepository, times(1)).getCeAgainstCmList(cmUsername);
	    }
	    
	    @Test
	    void testGetTeamLeaderboardFilterData() {
	        // Arrange
	        List<SortFilterData> sortFilterDataMock = List.of(
	            new SortFilterData(1L, "SortingData1"),
	            new SortFilterData(2L, "SortingData2")
	        );
	        
	        TargetRange targetRangeMock = new TargetRange(1, 10, 100);

	        when(sortFilterDataRepostiory.findAll()).thenReturn(sortFilterDataMock);
	        when(targetRangeRepository.findById(1)).thenReturn(Optional.of(targetRangeMock));

	        // Act
	        TeamLeaderboardFilterDataDto result = teamLeaderBoardService.getTeamLeaderboardFilterData();

	        // Assert
	        assertNotNull(result);
	        assertEquals(2, result.getSortFilterData().size());
	        assertEquals("SortingData1", result.getSortFilterData().get(0).getFilterData());
	        assertEquals("SortingData2", result.getSortFilterData().get(1).getFilterData());

	        assertNotNull(result.getTargetRange());
	        assertEquals(10, result.getTargetRange().getMinAchieved());
	        assertEquals(100, result.getTargetRange().getMaxAchieved());

	        // Verify method calls
	        verify(sortFilterDataRepostiory, times(1)).findAll();
	        verify(targetRangeRepository, times(1)).findById(1);
	    }
	    
	    @Test
	    void testGetTeamLeaderboardDetails() {
	        // Arrange
	        String username = "userA";
	        String paramMonthYear = "Jan 2025";
	        Integer sortTypeId = 1;
	        Integer targetAchievedMin = 50;
	        Integer targetAchievedMax = 100;

	        TeamLeaderboardRankDetails detail1 = new TeamLeaderboardRankDetails();
	        detail1.setRank(1);
	        detail1.setUserDisplayName("User A");
	        detail1.setUsername("userA");
	        detail1.setAchieved(85);
	        detail1.setTarget(100);
	        detail1.setIncentiveAmount("2000");
	        detail1.setConsistencyBonus("500");
	        detail1.setLocation("Pune");

	        List<TeamLeaderboardRankDetails> mockList = List.of(detail1);

	        when(teamLeaderboardRankDetailsRepostiory.getTeamLeaderboardRankDetails(
	            username, paramMonthYear, sortTypeId, targetAchievedMin, targetAchievedMax
	        )).thenReturn(mockList);

	        // Act
	        List<TeamLeaderboardDetailsDto> result = teamLeaderBoardService.getTeamLeaderboardDetails(
	            username, paramMonthYear, sortTypeId, targetAchievedMin, targetAchievedMax
	        );

	        // Assert
	        assertNotNull(result);
	        assertEquals(1, result.size());

	        TeamLeaderboardDetailsDto dto = result.get(0);
	        assertEquals("User A", dto.getUserDisplayName());
	        assertEquals(1, dto.getRank());
	        assertEquals(85, dto.getAchieved());
	        assertEquals("Pune", dto.getLocation());

	        // Match expected URL format from service logic
	        String expectedProfilePicUrl = "https://ops360dev.hitachi-payments.com/ops/gateway/feign-client/get-profile-picture-file/userA";
	        assertEquals(expectedProfilePicUrl, dto.getProfilePic());

	        assertEquals("2000", dto.getIncentive());
	        assertEquals("500", dto.getConsistencyBonus());
	    }

	    
	    @Test
	    void testGetTeamLeaderboardDetailsWithEmptyData() {
	        // Arrange
	        String token = "dummy-token";
	        String username = "user1";
	        String paramMonthYear = "Jan 2025";
	        Integer sortTypeId = 1;
	        Integer targetAchievedMin = 50;
	        Integer targetAchievedMax = 100;

	        TeamLeaderboardDetailsReqDto reqDto = new TeamLeaderboardDetailsReqDto();
	        reqDto.setUsername(username);
	        reqDto.setParamMonthYear(paramMonthYear);
	        reqDto.setSortTypeId(sortTypeId);
	        reqDto.setTargetAchievedMin(targetAchievedMin);
	        reqDto.setTargetAchievedMax(targetAchievedMax);

	        // Mocking the repository call for an empty list
	        when(teamLeaderboardRankDetailsRepostiory.getTeamLeaderboardRankDetails(
	            username, paramMonthYear, sortTypeId, targetAchievedMin, targetAchievedMax
	        )).thenReturn(List.of());

	        // Act
	        List<TeamLeaderboardDetailsDto> result = teamLeaderBoardService.getTeamLeaderboardDetails(
	            reqDto.getUsername(), reqDto.getParamMonthYear(), reqDto.getSortTypeId(), reqDto.getTargetAchievedMin(), reqDto.getTargetAchievedMax()
	        );

	        // Assert
	        assertNotNull(result);
	        assertTrue(result.isEmpty());

	        verify(teamLeaderboardRankDetailsRepostiory, times(1)).getTeamLeaderboardRankDetails(
	            username, paramMonthYear, sortTypeId, targetAchievedMin, targetAchievedMax
	        );
	    }
	    

}
