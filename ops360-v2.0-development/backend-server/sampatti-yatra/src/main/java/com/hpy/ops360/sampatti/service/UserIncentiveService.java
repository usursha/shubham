package com.hpy.ops360.sampatti.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.keycloakbase.util.LoginUtil;
import com.hpy.ops360.sampatti.dto.UserFinancialYearIncentiveDto;
import com.hpy.ops360.sampatti.entity.UserFinancialYearIncentiveEntity;
import com.hpy.ops360.sampatti.repository.UserIncentiveRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserIncentiveService {

    @Autowired
    private UserIncentiveRepository repository;

    @Autowired
    private LoginUtil util;

    public List<UserFinancialYearIncentiveDto> getFinancialYearIncentives() {
        log.info("Entering getFinancialYearIncentives method");

        String username = util.getLoggedInUserName();
        log.debug("Retrieved logged-in username: {}", username);

        List<UserFinancialYearIncentiveEntity> resultList = repository.getUserFinancialYearIncentives(username);
        log.debug("Fetched {} rows from the database for user: {}", resultList.size(), username);

        List<UserFinancialYearIncentiveDto> responseList = resultList.stream()
                .map(data -> new UserFinancialYearIncentiveDto(data.getSrno(), data.getYear(), data.getIncentiveAmount()))
                .collect(Collectors.toList());

        log.info("Returning financial year incentive data with {} records for user: {}", responseList.size(), username);
        log.debug("Response: {}", responseList);

        return responseList;
    }
}
