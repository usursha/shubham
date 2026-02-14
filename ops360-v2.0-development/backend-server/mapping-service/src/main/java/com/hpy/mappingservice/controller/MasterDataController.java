package com.hpy.mappingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.mappingservice.response.dto.AbsenceSlotDTO;
import com.hpy.mappingservice.response.dto.LeaveTypeDTO;
import com.hpy.mappingservice.response.dto.ReasonDTO;
import com.hpy.mappingservice.service.MasterDataService;
import com.hpy.mappingservice.utils.RestUtilImpl;
import com.hpy.rest.dto.IResponseDto;

@RestController
@RequestMapping("/v2/app/api/masterdata")
public class MasterDataController {
    @Autowired
    private MasterDataService masterDataService;
    
    
    @Autowired
	private RestUtilImpl restUtils;

    @GetMapping("/leave-types")
    public IResponseDto getLeaveTypes() {
    	List<LeaveTypeDTO> data=masterDataService.getAllLeaveTypes();
    	
        return restUtils.wrapResponse(data, "fetched Response of Leave Types");
    }

    @GetMapping("/reasons")
    public IResponseDto getReasons() {
    	
    	List<ReasonDTO> data=masterDataService.getAllReasons();
    	return restUtils.wrapResponse(data, "fetched Response of Reasons");
    }

    @GetMapping("/absence-slots")
    public IResponseDto getAbsenceSlots() {
    	List<AbsenceSlotDTO> data=masterDataService.getAllAbsenceSlots();
        return restUtils.wrapResponse(data, "fetched Response of Absence Slots");
    }
}