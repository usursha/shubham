package com.hpy.ops360.synergyservice.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hpy.ops360.synergyservice.request.dto.AtmUptimeRequest;
import com.hpy.ops360.synergyservice.request.dto.RemarksrequestDto;
import com.hpy.ops360.synergyservice.request.dto.SynergyLoginRequest;
import com.hpy.ops360.synergyservice.request.dto.TicketHistoryDto;
import com.hpy.ops360.synergyservice.request.dto.TicketHistoryRequest;
import com.hpy.ops360.synergyservice.request.dto.UpdateSubcallTypeDto;
import com.hpy.ops360.synergyservice.request.dto.UpdateSubcallTypeReq;
import com.hpy.ops360.synergyservice.request.dto.UpdateTicketStatusDto;
import com.hpy.ops360.synergyservice.request.dto.UpdateTicketStatusReq;
import com.hpy.ops360.synergyservice.response.dto.AtmHistoryNTicketsResponse;
import com.hpy.ops360.synergyservice.response.dto.AtmUptimeDetailsResp;
import com.hpy.ops360.synergyservice.response.dto.GenericResponseDto;
import com.hpy.ops360.synergyservice.response.dto.RemarksHistoryDto;
import com.hpy.ops360.synergyservice.response.dto.RemarksResponseDto;
import com.hpy.ops360.synergyservice.response.dto.SynergyResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SynergyService {
	
	@Value("${synergy.base-url}")
	private String synergyBaseUrl;

	@Value("${synergy.username}")
	private String username;

	@Value("${synergy.password}")
	private String password;

	@Autowired
	private RestTemplate restTemplate;
	
	public String getSynergyRequestId() {
		String url = synergyBaseUrl + "/trequest";
		SynergyResponse response = restTemplate.postForObject(url, new SynergyLoginRequest(username, password),
				SynergyResponse.class);
		log.info("Fetched new SynergyResponse: {}", response);
		return response.getRequestid();
	}
	
	public AtmUptimeDetailsResp getAtmUptime(String atmid) {

		String url = synergyBaseUrl + "/getatmuptime";

		AtmUptimeRequest atmUptimeRequest = new AtmUptimeRequest(getSynergyRequestId(), atmid);

		return restTemplate.postForObject(url, atmUptimeRequest, AtmUptimeDetailsResp.class);

	}
	
	public RemarksResponseDto getRemarksHistory(RemarksrequestDto requestsdto) {
		log.info("getRemarksHistory| requestsdto:{}", requestsdto);
//		DisableSslClass.disableSSLVerification();
		String url = synergyBaseUrl + "/gettickethistory";

		RemarksHistoryDto request = new RemarksHistoryDto(getSynergyRequestId(),
				requestsdto.getTicketNo(), requestsdto.getAtmId());
		log.info("getRemarksHistory| synRequest:{}", request);
		RemarksResponseDto remarksResponseDto = restTemplate.postForObject(url, request, RemarksResponseDto.class);
		log.info("getRemarksHistory| synResponse:{}", remarksResponseDto);
		return remarksResponseDto;
	}
	
	public AtmHistoryNTicketsResponse getntickets(TicketHistoryDto ticketHistoryReqDto) {

		// API for Get ticket history based on ATM ID and n tickets
		String url = synergyBaseUrl + "/getntickets";

		TicketHistoryRequest ticketHistoryRequest = new TicketHistoryRequest(getSynergyRequestId(),
				ticketHistoryReqDto.getAtmid(), ticketHistoryReqDto.getNticket());
		log.info("SynergyService:getntickets Request:{}", ticketHistoryRequest);
		AtmHistoryNTicketsResponse atmHistoryNTicketsResponse = restTemplate.postForObject(url, ticketHistoryRequest,
				AtmHistoryNTicketsResponse.class);
		log.info("SynergyService:getntickets Response:{}", atmHistoryNTicketsResponse);
		return atmHistoryNTicketsResponse;
	}
	
	public GenericResponseDto updateTicketStatus(UpdateTicketStatusDto updateTicketStatusDto) {
		log.info("updateTicketStatus|updateTicketStatusDto:{}", updateTicketStatusDto);

		String url = synergyBaseUrl + "/updateticketstatus";

		UpdateTicketStatusReq updateTicketStatus = UpdateTicketStatusReq.builder()
				.requestid(getSynergyRequestId()).atmId(updateTicketStatusDto.getAtmId())
				.ticketNo(updateTicketStatusDto.getTicketNo()).comment(updateTicketStatusDto.getComment())
				.document(updateTicketStatusDto.getDocument()).documentName(updateTicketStatusDto.getDocumentName())
				.updatedBy(updateTicketStatusDto.getUpdatedby()).build();

		log.info("updateTicketStatus|updateTicketStatusReq:{}", updateTicketStatus);
		SynergyResponse response = restTemplate.postForObject(url, updateTicketStatus, SynergyResponse.class);
		log.info("updateSubcallType|response:{}", response);
		if (response.getStatus().equalsIgnoreCase("success")) {
			return new GenericResponseDto(response.getStatus(), "comment added with image ");
		}
		return new GenericResponseDto(response.getStatus(), "Invalid Request Id");

	}

	public SynergyResponse updateSubcallType(UpdateSubcallTypeDto updateSubcallTypeDto) {
		log.info("updateSubcallType|updateSubcallTypeDto:{}", updateSubcallTypeDto);
		String url = synergyBaseUrl + "/updatesubcalltype";
		
		UpdateSubcallTypeReq request = UpdateSubcallTypeReq.builder().requestid(getSynergyRequestId()).ticketNo(updateSubcallTypeDto.getTicketNo()).atmId(updateSubcallTypeDto.getAtmId()).subcallType(updateSubcallTypeDto.getSubcallType()).comments(updateSubcallTypeDto.getComments()).updatedBy(updateSubcallTypeDto.getUpdatedBy()).build();
		SynergyResponse response = restTemplate.postForObject(url, request, SynergyResponse.class);
		log.info("updateSubcallType|response:{}", response);
		return response;
	}
	

}
