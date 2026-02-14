package com.hpy.mappingservice.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.ActiveLeaveUserEntity;
import com.hpy.mappingservice.repository.ActiveLeaveUserRepository;
import com.hpy.mappingservice.response.dto.ActiveLeaveUserDto;
import com.hpy.mappingservice.response.dto.CityFilterDto;
import com.hpy.mappingservice.response.dto.LeaveDateRangeFilterDto;
import com.hpy.mappingservice.response.dto.LeaveFilterResponseDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActiveLeaveUserService {

    @Autowired
    private ActiveLeaveUserRepository leaveUserRepository;
    
    @Autowired
	private LoginService loginService;

    public List<ActiveLeaveUserDto> getActiveLeaveUsers() {
        
        String managerUsername = loginService.getLoggedInUser();
        log.info("Fetching active leave users for manager: {}", managerUsername);
        
        List<ActiveLeaveUserEntity> projections = leaveUserRepository.getActiveLeaveUsersByManager(managerUsername);

        List<ActiveLeaveUserDto> response = projections.stream().map(p -> {
            ActiveLeaveUserDto dto = new ActiveLeaveUserDto();
            dto.setSrNo(p.getSrNo());
            dto.setUserId(p.getUserId());
            dto.setEmployeeCode(p.getEmployeeCode());
            dto.setUserName(p.getUserName());
            dto.setLeaveId(p.getLeaveId());
            dto.setStartRange(p.getStartRange());
            dto.setEndRange(p.getEndRange());
            dto.setRawStarttime(p.getRawStarttime());
            dto.setRawEndtime(p.getRawEndtime());
            dto.setFullName(p.getFullName());
            dto.setEmailId(p.getEmailId());
            dto.setCity(p.getCity());
            dto.setAddress(p.getAddress());
            dto.setMappedAtm(p.getMappedAtm());
            dto.setTotalAtm(p.getTotalAtm());
            dto.setTempCe(p.getTempCe());
            dto.setProfilePic(p.getProfilePic());
            return dto;
        }).collect(Collectors.toList());

        log.info("Fetched {} active leave users for manager {}", response.size(), managerUsername);
        return response;
    }
    
    
    public LeaveFilterResponseDto getLeaveFilters() {
        List<ActiveLeaveUserDto> allLeaves = getActiveLeaveUsers();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        // ✅ Count leaves active today
        int todayCount = (int) allLeaves.stream()
            .filter(dto -> {
                LocalDate start = dto.getRawStarttime().toLocalDate();
                LocalDate end = dto.getRawEndtime().toLocalDate();
                return !start.isAfter(today) && !end.isBefore(today);
            })
            .count();

        // ✅ Count leaves overlapping with this week
        int thisWeekCount = (int) allLeaves.stream()
            .filter(dto -> {
                LocalDate start = dto.getRawStarttime().toLocalDate();
                LocalDate end = dto.getRawEndtime().toLocalDate();
                return !end.isBefore(startOfWeek) && !start.isAfter(endOfWeek);
            })
            .count();

        int allCount = allLeaves.size();

        List<LeaveDateRangeFilterDto> dateFilters = List.of(
            new LeaveDateRangeFilterDto("All", allCount),
            new LeaveDateRangeFilterDto("Today", todayCount),
            new LeaveDateRangeFilterDto("This Week", thisWeekCount)
        );

        Map<String, Long> cityCounts = allLeaves.stream()
            .filter(dto -> dto.getCity() != null && !dto.getCity().isBlank())
            .collect(Collectors.groupingBy(ActiveLeaveUserDto::getCity, Collectors.counting()));

        List<CityFilterDto> cityFilters = cityCounts.entrySet().stream()
            .map(entry -> new CityFilterDto(entry.getKey(), entry.getValue().intValue()))
            .collect(Collectors.toList());

        LeaveFilterResponseDto response = new LeaveFilterResponseDto();
        response.setLeaveDateRangeFilters(dateFilters);
        response.setCityFilters(cityFilters);

        return response;
    }




}
