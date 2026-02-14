//package com.hpy.ops360.sampatti.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.hpy.ops360.sampatti.dto.DataResponseDto;
//import com.hpy.ops360.sampatti.dto.IResponseDtoImpl;
//import com.hpy.ops360.sampatti.dto.LeaderBoardRespCountDto;
//import com.hpy.ops360.sampatti.dto.LeaderboardRequestDto;
//import com.hpy.ops360.sampatti.dto.LeaderboardResponseDto;
//import com.hpy.ops360.sampatti.dto.UserProfilePictureDto;
//import com.hpy.ops360.sampatti.entity.LeaderboardEntity;
//import com.hpy.ops360.sampatti.repository.AssetRepo;
//import com.hpy.ops360.sampatti.repository.LeaderboardRepository;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//
//@ExtendWith(MockitoExtension.class)
//@Slf4j
//class LeaderboardServiceTest {
//
//
//    @Mock
//    private LeaderboardRepository leaderboardRepository;
//
//    @Mock
//    private AssetRepo assetRepo;
//
//    @Mock
//    private HttpServletRequest httpRequest;
//
//    @InjectMocks
//    private LeaderboardService leaderboardService;
//
//    private LeaderboardRequestDto requestDto;
//    private LeaderboardEntity leaderboardEntity;
//
//    @BeforeEach
//    void setup() {
//        log.info("Setting up test data...");
//
//        requestDto = new LeaderboardRequestDto();
//        requestDto.setMonthYear("Feb 2025");
//        requestDto.setUserType("EMPLOYEE");
//        requestDto.setSearchKeyword("john");
//        requestDto.setSortOrder("1"); // should convert to "desc"
//        requestDto.setAchievedMin(0);
//        requestDto.setAchievedMax(1000);
//        requestDto.setZone("East");
//        requestDto.setState("Bihar");
//        requestDto.setCity("Patna");
//        requestDto.setPageIndex(0);
//        requestDto.setPageSize(10);
//
//        leaderboardEntity = new LeaderboardEntity();
//        leaderboardEntity.setSrno(1L);
//        leaderboardEntity.setAllIndiaRank(10);
//        leaderboardEntity.setFullName("John Doe");
//        leaderboardEntity.setLoginId("jdoe");
//        leaderboardEntity.setTarget(1000);
//        leaderboardEntity.setAchieved(950);
//        leaderboardEntity.setDifferenceTarget(50);
//        leaderboardEntity.setReward("Gold");
//        leaderboardEntity.setIncentiveAmount("5000");
//        leaderboardEntity.setConsistencyAmount("1000");
//        leaderboardEntity.setLocation("Patna");
//        leaderboardEntity.setProfileLocation("HQ");
//        leaderboardEntity.setReportsTo("Jane Smith");
//        leaderboardEntity.setNationalHead("NHead");
//        leaderboardEntity.setZonalHead("ZHead");
//        leaderboardEntity.setStateHead("SHead");
//        leaderboardEntity.setChannelManager("Manager");
//    }
//
//    @Test
//    void testGetLeaderboardData_Success() {
//        log.info("Running test: testGetLeaderboardData_Success");
//
//        // Mock Authorization Header
//        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer test-token");
//
//        // Mock repository count query
//        when(leaderboardRepository.getLeaderboardcountData(
//                anyString(), anyString(), anyString(), anyString(),
//                anyInt(), anyInt(), anyString(), anyString(), anyString()))
//                .thenReturn(List.of(leaderboardEntity));
//
//        // Mock repository paged result query
//        when(leaderboardRepository.getLeaderboardData(
//                anyString(), anyString(), anyString(), anyString(),
//                anyInt(), anyInt(), anyString(), anyString(), anyString(),
//                anyInt(), anyInt()))
//                .thenReturn(List.of(leaderboardEntity));
//
//        // Mock Feign call for profile picture
//        DataResponseDto data = new DataResponseDto();
//        data.setImageId("img123");
//
//        IResponseDtoImpl responseDto = new IResponseDtoImpl();
//        responseDto.setData(data);
//
//        when(assetRepo.getPhotoByUsername(anyString(), any(UserProfilePictureDto.class)))
//                .thenReturn(responseDto);
//
//        // Call the method
//        LeaderBoardRespCountDto result = leaderboardService.getLeaderboardData(httpRequest, requestDto);
//
//        // Assertions
//        assertNotNull(result);
//        assertEquals(1, result.getTotalcounts());
//        assertEquals(1, result.getFilterdata().size());
//
//        LeaderboardResponseDto dto = result.getFilterdata().get(0);
//        assertEquals("John Doe", dto.getFullName());
//        assertEquals("img123", dto.getProfilePic());
//
//        log.info("Test passed with leaderboard user: {} and imageId: {}", dto.getFullName(), dto.getProfilePic());
//    }
//
//    @Test
//    void testGetLeaderboardData_ProfilePictureFails() {
//        log.info("Running test: testGetLeaderboardData_ProfilePictureFails");
//
//        when(httpRequest.getHeader("Authorization")).thenReturn("Bearer test-token");
//        when(leaderboardRepository.getLeaderboardcountData(any(), any(), any(), any(), any(), any(), any(), any(), any()))
//                .thenReturn(List.of(leaderboardEntity));
//
//        when(leaderboardRepository.getLeaderboardData(any(), any(), any(), any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
//                .thenReturn(List.of(leaderboardEntity));
//
//        when(assetRepo.getPhotoByUsername(any(), any())).thenThrow(new RuntimeException("Feign error"));
//
//        LeaderBoardRespCountDto result = leaderboardService.getLeaderboardData(httpRequest, requestDto);
//
//        assertNotNull(result);
//        assertEquals(1, result.getFilterdata().size());
//        assertEquals("", result.getFilterdata().get(0).getChildren().get(0).getProfilePic());
//
//        log.info("Profile picture fetch failed as expected, default empty profilePic returned.");
//    }
//}
