package com.hpy.monitoringservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.monitoringservice.dto.ControllerStatsDTO;
import com.hpy.monitoringservice.entity.ControllerStats;
import com.hpy.monitoringservice.repository.ControllerStatsRepository;


@Service
public class ControllerStatsService {
	
    @Autowired
    private ControllerStatsRepository repository;

    public List<ControllerStatsDTO> getStatsByControllerName(String controllerName, String fromDate, String toDate) {
        List<ControllerStats> stats = repository.findStatsByControllerName(controllerName, fromDate, toDate);
        return stats.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ControllerStatsDTO convertToDTO(ControllerStats stats) {
        ControllerStatsDTO dto = new ControllerStatsDTO();
        dto.setUserName(stats.getUserName());
        dto.setCallCount(stats.getCallCount());
        dto.setAvgTimeTaken(stats.getAvgTimeTaken());
        dto.setMaxTimeTaken(stats.getMaxTimeTaken());
        return dto;
    }
}