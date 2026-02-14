package com.hpy.monitoringservice.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.monitoringservice.dto.AllControllerStatsDto;
import com.hpy.monitoringservice.dto.ControllerDetailsDTO;
import com.hpy.monitoringservice.dto.ControllerDetailsRequest;
import com.hpy.monitoringservice.dto.ControllerNameRequest;
import com.hpy.monitoringservice.dto.ControllerStatsDTO;
import com.hpy.monitoringservice.dto.FilterRequest;
import com.hpy.monitoringservice.service.ApiLogService;
import com.hpy.monitoringservice.service.ControllerDetailsService;
import com.hpy.monitoringservice.service.ControllerStatsService;
import com.hpy.monitoringservice.service.MonitoringService;

@RestController
@RequestMapping("/monitor")
@CrossOrigin("${app.cross-origin.allow}")
public class MonitoringController {
	
	@Autowired
	private MonitoringService monitoringService;
	
	@Autowired
    private ApiLogService apiLogService;
    
    @Autowired
    private ControllerStatsService controllerStatsService;
    
    @Autowired
    private ControllerDetailsService controllerDetailsService;
    
    @GetMapping("/open-ticket-process")
	public ResponseEntity<Map<String,String>> getOpenTicketMonitorDetails()
	{
		return ResponseEntity.ok(monitoringService.getOpenTicketMonitorDetails());
	}

    @PostMapping("/allcontroller")
    public ResponseEntity<List<AllControllerStatsDto>> getControllerStats(@RequestBody FilterRequest request) {
    	List<AllControllerStatsDto> response=apiLogService.getControllerStats(request.getFromDate(),request.getToDate());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/controllerlist")
    public ResponseEntity<List<ControllerStatsDTO>> getStatsByControllerName(@RequestBody ControllerNameRequest request) {
        List<ControllerStatsDTO> stats = controllerStatsService.getStatsByControllerName(request.getControllerName(), request.getFromDate(), request.getToDate());
        return ResponseEntity.ok(stats);
    }
    
    @PostMapping("/controller/details")
    public ResponseEntity<List<ControllerDetailsDTO>> getControllerDetailsByUser(@RequestBody ControllerDetailsRequest request) {
        List<ControllerDetailsDTO> details = controllerDetailsService.getControllerDetailsByUser(request.getControllerName(), request.getUserName(), request.getFromDate(), request.getToDate());
        return ResponseEntity.ok(details);
    }
	
	

}
