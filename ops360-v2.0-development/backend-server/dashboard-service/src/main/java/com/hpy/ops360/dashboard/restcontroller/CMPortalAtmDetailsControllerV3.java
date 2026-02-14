package com.hpy.ops360.dashboard.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.dto.ATMIndexDetailsDTO;
import com.hpy.ops360.dashboard.dto.AtmDetailsDtowrapper;
import com.hpy.ops360.dashboard.dto.CEDetailsDto;
import com.hpy.ops360.dashboard.dto.CMUnderCEAtmDetailsDTO;
import com.hpy.ops360.dashboard.dto.GetCmPortalATMScreenDataDto;
import com.hpy.ops360.dashboard.dto.TicketsRaisedCountDTO;
import com.hpy.ops360.dashboard.entity.CEDetails;
import com.hpy.ops360.dashboard.entity.TicketsRaisedCount;
import com.hpy.ops360.dashboard.service.CmSynopsisService;
import com.hpy.ops360.dashboard.service.DashboardService;
import com.hpy.rest.dto.ResponseDto;

import io.micrometer.core.annotation.Timed;
import lombok.extern.log4j.Log4j2;


@Log4j2
@RestController
@RequestMapping("Cm_portalATMDetails2")
@CrossOrigin("${app.cross-origin.allow}")
public class CMPortalAtmDetailsControllerV3 {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private CmSynopsisService cmSynopsisService;

	@GetMapping("uptime_active/{userId}")
	public ResponseEntity<ResponseDto<GetCmPortalATMScreenDataDto>> getAtmScreenDataForCm(@PathVariable String userId) {
		GetCmPortalATMScreenDataDto atmUptimeAndActiveMachineDto = dashboardService.getCmPortalATMScreenData(userId);

		ResponseDto<GetCmPortalATMScreenDataDto> formattedResponse = new ResponseDto<>();
		
		
		if (atmUptimeAndActiveMachineDto != null) {
			formattedResponse.setResponseCode(HttpStatus.OK.value());
	        formattedResponse.setMessage("Successfully fetched the Data of ATM Screen Data for CM");
	        formattedResponse.setData(atmUptimeAndActiveMachineDto);
			return ResponseEntity.ok(formattedResponse);
		} else {
			
			formattedResponse.setResponseCode(HttpStatus.OK.value());
	        formattedResponse.setMessage("Cannot Fetched the data For respective User");
	        formattedResponse.setData(null);
			return ResponseEntity.status(401).body(formattedResponse);
		}
	}
	
	
	@GetMapping("/Ce_details/{userId}")
	public ResponseEntity<ResponseDto<CEDetailsDto>> getPersonalOfficialDetails(@PathVariable String userId) {
		
		CEDetailsDto response=cmSynopsisService.getCEDetails(userId);
		ResponseDto<CEDetailsDto> formattedResponse = new ResponseDto<>();
		formattedResponse.setResponseCode(HttpStatus.OK.value());
        formattedResponse.setMessage("Successfully fetched the CE Details as Per User ID");
        formattedResponse.setData(response);
		return ResponseEntity.ok(formattedResponse);
	}
	
	
	@GetMapping("/counts/{userId}")
	public ResponseEntity<ResponseDto<TicketsRaisedCountDTO>> getTicketsRaisedCount(@PathVariable String userId) {
		TicketsRaisedCountDTO ticketCounts = dashboardService.getCETicketsRaisedCount(userId);
		
		ResponseDto<TicketsRaisedCountDTO> formattedResponse = new ResponseDto<>();
		formattedResponse.setResponseCode(HttpStatus.OK.value());
        formattedResponse.setMessage("Successfully fetched the Ticket Raised Count as Per User ID");
        formattedResponse.setData(ticketCounts);
		return ResponseEntity.ok(formattedResponse);

	}
	
	
	@GetMapping("/get_CMUnderCEAtmDetails/{userId}")
	public ResponseEntity<ResponseDto<AtmDetailsDtowrapper>> getCMUnderCEAtmDetails(@PathVariable String userId) {
		List<CMUnderCEAtmDetailsDTO> atmDetailsDTO = cmSynopsisService.getCMUnderCEAtmDetails(userId);
		
		ResponseDto<AtmDetailsDtowrapper> formattedResponse = new ResponseDto<>();
		
		
		AtmDetailsDtowrapper datawrapper=new AtmDetailsDtowrapper();
		datawrapper.setAtmdetailsdtolist(atmDetailsDTO);


        formattedResponse.setResponseCode(HttpStatus.OK.value());
        formattedResponse.setMessage("Fetched Remarks Data");
        formattedResponse.setData(datawrapper);
		return ResponseEntity.ok(formattedResponse);
	}
	
	@GetMapping("/ticket_details/{atmId}")
	public ResponseEntity<ResponseDto<ATMIndexDetailsDTO>> getATMIndexDetails(@PathVariable String atmId) {
		
		ATMIndexDetailsDTO response=cmSynopsisService.getATMIndexDetails(atmId);
		ResponseDto<ATMIndexDetailsDTO> formattedResponse = new ResponseDto<>();
		formattedResponse.setResponseCode(HttpStatus.OK.value());
        formattedResponse.setMessage("Successfully fetched the Ticket Data of ATM ID");
        formattedResponse.setData(response);
		return ResponseEntity.ok(formattedResponse);
	}
}
