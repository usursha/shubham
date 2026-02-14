package com.hpy.ops360.sampatti.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.keycloakbase.util.LoginUtil;
import com.hpy.ops360.sampatti.dto.ScoreCardResponseDto;
import com.hpy.ops360.sampatti.entity.ScoreCardResponseEntity;
import com.hpy.ops360.sampatti.repository.ScoreCardRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScoreCardService {

    @Autowired
    private ScoreCardRepository repository;
//    
//   @Autowired
//   private LoginService loginService;
	@Autowired
	private LoginUtil util;

    public List<ScoreCardResponseDto> getScoreCardDetails() {
        log.info("***** Inside getScoreCardDetails Service *****");
        String request=util.getLoggedInUserName();
        log.info("Request Received: {}", request);

        List<ScoreCardResponseEntity> result = repository.getScoreCardData(request);

        List<ScoreCardResponseDto> response = result.stream().map(data -> new ScoreCardResponseDto(
                data.getSrno(),
        		data.getAtmceoMonthlyIncentiveId(),
                data.getMonthDate(),
                data.getUserLoginId(),
                data.getTarget(),
                data.getAchieved(),
                data.getRank(),
                data.getIncentiveAmount(),
                data.getRoleId(),
                data.getRankImagePath(),
                data.getUpdatedDate()
        )).toList();

        log.info("Response Returned: {}", response);
        return response;
    }
}
