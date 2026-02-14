package com.hpy.monitoringservice.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.monitoringservice.dto.AllControllerStatsDto;
import com.hpy.monitoringservice.entity.AllControllerStatsEntity;
import com.hpy.monitoringservice.repository.ControllerstatsRepo;

import java.util.List;

@Service
public class ApiLogService {

    @Autowired
    private ControllerstatsRepo controllerstatsRepo;

    public List<AllControllerStatsDto> getControllerStats(String fromDate, String toDate) {
    	List<AllControllerStatsEntity> data=controllerstatsRepo.getControllerStatsEntity(fromDate,toDate);
    	
    	
    	List<AllControllerStatsDto> response=data.stream().map(result -> new AllControllerStatsDto(result.getControllerName(),result.getCallCount(),result.getAvgTimeTaken(),result.getMaxTimeTaken())).toList();
    	
    	
        return response;
    }
}