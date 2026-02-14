package com.hpy.ops360.sampatti.service;

import com.hpy.ops360.sampatti.dto.LeadBoardDetailsDto;
import com.hpy.ops360.sampatti.dto.LeadBoardRequestDto;
import com.hpy.ops360.sampatti.entity.LeadBoardDetailsEntity;
import com.hpy.ops360.sampatti.repository.LeadBoardDetailsRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LeadBoardDetailsService {

    @Autowired
    private LeadBoardDetailsRepository repository;

    public List<LeadBoardDetailsDto> getLeadBoardData(LeadBoardRequestDto dto) {
        log.info("***** Inside getLeadBoardData Service *****");

        List<LeadBoardDetailsEntity> result = repository.getLeadBoardDetails(dto.getRoleId(), dto.getMonthName());

        List<LeadBoardDetailsDto> response = result.stream().map(data -> new LeadBoardDetailsDto(
                data.getAtmceoMonthlyIncentiveId(),
                data.getMonthDate(),
                data.getUserLoginId(),
                data.getTarget(),
                data.getAchieved(),
                data.getRank(),
                data.getIncentiveAmount(),
                data.getRoleId(),
                data.getCity(),
                data.getRoleCode(),
                data.getRankImagePath()
        )).toList();

        log.info("Response Returned: {}", response);
        return response;
    }
}
