package com.hpy.ops360.sampatti.service;

import com.hpy.keycloakbase.util.LoginUtil;
import com.hpy.ops360.sampatti.dto.HierarchyStatsDto;
import com.hpy.ops360.sampatti.entity.HierarchyStatsEntity;
import com.hpy.ops360.sampatti.repository.HierarchyStatsRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HierarchyStatsService {

    private HierarchyStatsRepository repository;

    private LoginUtil loginUtil;
    
	public HierarchyStatsService(HierarchyStatsRepository repository, LoginUtil loginUtil) {
		super();
		this.repository = repository;
		this.loginUtil = loginUtil;
	}

    public HierarchyStatsDto getHierarchyStats() {
        log.info("Entering getHierarchyStats method");

        String username = loginUtil.getLoggedInUserName();
        log.debug("Retrieved logged-in username: {}", username);

        HierarchyStatsEntity entity = repository.getHierarchyStats(username);
        HierarchyStatsDto response = new HierarchyStatsDto(
                entity.getSrno(),
                entity.getLevel(),
                entity.getSuperiorUserId(),
                entity.getTeamCount(),
                entity.getTotalCount()
        );


        

        log.info("Returning hierarchy stats for user: {}", username);
        log.info("Response: {}", response);

        return response;
    }




}
