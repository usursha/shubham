package com.hpy.mappingservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.CEAreaIndexEntity;
import com.hpy.mappingservice.entity.CECityIndexEntity;
import com.hpy.mappingservice.entity.CEIndexEntity;
import com.hpy.mappingservice.entity.CEMTDTargetIndexEntity;
import com.hpy.mappingservice.entity.CESearchIndexEntity;
import com.hpy.mappingservice.repository.CEAreaIndexRepository;
import com.hpy.mappingservice.repository.CECityIndexRepository;
import com.hpy.mappingservice.repository.CEIndexRepository;
import com.hpy.mappingservice.repository.CEMTDTargetIndexRepository;
import com.hpy.mappingservice.repository.CESerachIndexRepository;
import com.hpy.mappingservice.request.dto.CEIndexRequestDto;
import com.hpy.mappingservice.response.dto.CEAreaIndexResponseDTO;
import com.hpy.mappingservice.response.dto.CECityIndexResponseDTO;
import com.hpy.mappingservice.response.dto.CEIndexResponseDto;
import com.hpy.mappingservice.response.dto.CEMTDTargetIndexResponseDTO;
import com.hpy.mappingservice.response.dto.CESearchIndexResponseDto;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class CEIndexService {
	
	@Autowired
    private  CEIndexRepository ceIndexRepository;
	
	@Autowired
	private CESerachIndexRepository ceSerachIndexRepository;
	
	@Autowired
	private CEAreaIndexRepository ceAreaIndexRepository;
	
	@Autowired
	private CECityIndexRepository ceCityIndexRepository;
	
	@Autowired
	private CEMTDTargetIndexRepository ceMTDTargetIndexRepository;
    @Autowired
	private LoginService loginService;
//    public CEIndexService(CEIndexRepository ceIndexRepository) {
//        this.ceIndexRepository = ceIndexRepository;
//    }
    
    public List<CEIndexResponseDto> getCeIndexDetails(CEIndexRequestDto reqDTO) {
        log.info("Attempting to retrieve CE index details for cmUserid: {}"); 
        List<CEIndexEntity> entities = ceIndexRepository.getCeIndexDetails(loginService.getLoggedInUser(),reqDTO.getPageNumber(),reqDTO.getPageSize(),reqDTO.getSortOption(), reqDTO.getAvailability(),
        		reqDTO.getMtdTxnTargetStart(),reqDTO.getMtdTxnTargetEnd(),reqDTO.getCity(),
        		 reqDTO.getArea(),reqDTO.getSearch());

        if (entities == null || entities.isEmpty()) {
            log.info("No CE index entities found for cmUserid: {}" + reqDTO.getCity()+ "       "+ loginService.getLoggedInUser());
            return List.of(); 
        }  
        List<CEIndexResponseDto> response = entities.stream()
                .map(entity -> {                   
                    CEIndexResponseDto dto = new CEIndexResponseDto();
                    dto.setSrNo(entity.getSrNo());
                    dto.setChannelExecutiveName(entity.getChannelExecutiveName());
                    dto.setEmployeeCode(entity.getEmployeeCode());
                    dto.setCircleArea(entity.getCircleArea());
                    dto.setAvailabilityStatus(entity.getAvailabilityStatus());
                    dto.setAssignedNumberOfATMs(entity.getAssignedNumberOfATMs());
                    dto.setAtmAssignedIDs(entity.getAtmAssignedIDs());
                    dto.setDownMachines(entity.getDownMachines());
                    dto.setUptimeStatus(entity.getUptimeStatus());
                    dto.setMtdUptime(entity.getMtdUptime());
                    dto.setTransaction(entity.getTransaction());
                    dto.setTarget(entity.getTarget());
                    dto.setPercentageChange(entity.getPercentageChange());
                    dto.setTotalRecords(entity.getTotalRecords());
                    return dto;
                })
                .collect(Collectors.toList());

        log.info("Successfully retrieved and mapped {} CE index entries for cmUserid: {}", response.size());
        return response;
    
    }
    
    public List<CESearchIndexResponseDto> getCeSearchIndexDetails() {
        log.info("Attempting to retrieve CE index details for cmUserid: {}");
        List<CESearchIndexEntity> entities = ceSerachIndexRepository.getCeSearchIndexDetails(loginService.getLoggedInUser());

        if (entities == null || entities.isEmpty()) {
            log.info("No CE index entities found for cmUserid: {}");
            return List.of(); 
        }  
        List<CESearchIndexResponseDto> response = entities.stream()
                .map(entity -> {                   
                	CESearchIndexResponseDto dto = new CESearchIndexResponseDto();
                    dto.setSrNo(entity.getSrNo());
                    dto.setChannelExecutiveName(entity.getChannelExecutiveName());
                    dto.setEmployeeCode(entity.getEmployeeCode());
                    dto.setCircleArea(entity.getCircleArea());
                    dto.setAvailabilityStatus(entity.getAvailabilityStatus());
                    dto.setAssignedNumberOfATMs(entity.getAssignedNumberOfATMs());
                    dto.setAtmAssignedIDs(entity.getAtmAssignedIDs());
                    dto.setDownMachines(entity.getDownMachines());
                    dto.setUptimeStatus(entity.getUptimeStatus());
                    dto.setMtdUptime(entity.getMtdUptime());
                    dto.setTransaction(entity.getTransaction());
                    dto.setTarget(entity.getTarget());
                    dto.setPercentageChange(entity.getPercentageChange());
                    dto.setTotalRecords(entity.getTotalRecords());
                    dto.setCity(entity.getCity());
                    dto.setArea(entity.getArea());
                    return dto;
                })
                .collect(Collectors.toList());

        log.info("Successfully retrieved and mapped {} CE index entries for cmUserid: {}", response.size());
        return response;
    
    }
    

    public List<CEAreaIndexResponseDTO> getCEAreaIndexDetails() {
        log.info("Attempting to retrieve CE index details for cmUserid: {}");
        List<CEAreaIndexEntity> entities = ceAreaIndexRepository.getCEAreaIndexDetails(loginService.getLoggedInUser());

        if (entities == null || entities.isEmpty()) {
            log.info("No CE index entities found for cmUserid: {}");
            return List.of(); 
        }  
        List<CEAreaIndexResponseDTO> response = entities.stream()
                .map(entity -> {                   
                	CEAreaIndexResponseDTO dto = new CEAreaIndexResponseDTO();
                    dto.setSrNo(entity.getSrNo());
                    dto.setArea(entity.getArea());
                    return dto;
                })
                .collect(Collectors.toList());

        log.info("Successfully retrieved and mapped {} CE index entries for cmUserid: {}", response.size());
        return response;
    
    }
    
    public List<CECityIndexResponseDTO> getCECityIndexDetails() {
        log.info("Attempting to retrieve CE index details for cmUserid: {}");
        List<CECityIndexEntity> entities = ceCityIndexRepository.getCECityIndexDetails(loginService.getLoggedInUser());

        if (entities == null || entities.isEmpty()) {
            log.info("No CE index entities found for cmUserid: {}");
            return List.of(); 
        }  
        List<CECityIndexResponseDTO> response = entities.stream()
                .map(entity -> {                   
                	CECityIndexResponseDTO dto = new CECityIndexResponseDTO();
                    dto.setSrNo(entity.getSrNo());
                    dto.setCity(entity.getCity());
                    return dto;
                })
                .collect(Collectors.toList());

        log.info("Successfully retrieved and mapped {} CE index entries for cmUserid: {}", response.size());
        return response;
    
    }
    
    public List<CEMTDTargetIndexResponseDTO> getCEMTDTargetIndexDetails() {
        log.info("Attempting to retrieve CE index details for cmUserid: {}");
        List<CEMTDTargetIndexEntity> entities = ceMTDTargetIndexRepository.getCEMTDTargetIndexDetails(loginService.getLoggedInUser());

        if (entities == null || entities.isEmpty()) {
            log.info("No CE index entities found for cmUserid: {}");
            return List.of(); 
        }  
        List<CEMTDTargetIndexResponseDTO> response = entities.stream()
                .map(entity -> {                   
                	CEMTDTargetIndexResponseDTO dto = new CEMTDTargetIndexResponseDTO();
                    dto.setSrNo(entity.getSrNo());
                    dto.setStartTarget(entity.getStartTarget());
                    dto.setEndTarget(entity.getEndTarget());
                    return dto;
                })
                .collect(Collectors.toList());

        log.info("Successfully retrieved and mapped {} CE index entries for cmUserid: {}", response.size());
        return response;
    
    }
    
}