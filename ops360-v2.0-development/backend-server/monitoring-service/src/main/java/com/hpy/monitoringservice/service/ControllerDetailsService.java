package com.hpy.monitoringservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.monitoringservice.dto.ControllerDetailsDTO;
import com.hpy.monitoringservice.entity.ControllerDetails;
import com.hpy.monitoringservice.repository.ControllerDetailsRepository;

@Service
public class ControllerDetailsService {
	
    @Autowired
    private ControllerDetailsRepository repository;

    public List<ControllerDetailsDTO> getControllerDetailsByUser(String controllerName, String userName, String fromDate, String toDate) {
        List<ControllerDetails> details = repository.findDetailsByControllerNameAndUserName(controllerName, userName, fromDate, toDate);
        return details.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ControllerDetailsDTO convertToDTO(ControllerDetails details) {
        ControllerDetailsDTO dto = new ControllerDetailsDTO();
        dto.setSrno(details.getSrno());
        dto.setUserName(details.getUserName());
        dto.setControllerName(details.getControllerName());
        dto.setClassName(details.getClassName());
        dto.setMethodName(details.getMethodName());
        dto.setDateOfInsertion(details.getDateOfInsertion());
        dto.setRequest(details.getRequest());
        dto.setResponse(details.getResponse());
        dto.setStatusCode(details.getStatusCode());
        dto.setTimeTaken(details.getTimeTaken());
        return dto;
    }
}