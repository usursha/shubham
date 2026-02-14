package com.hpy.ops360.atmservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hpy.ops360.atmservice.dto.AtmHistoryNTicketsResponse;
import com.hpy.ops360.atmservice.dto.AtmIdDto;
import com.hpy.ops360.atmservice.dto.TicketHistoryDto;
import com.hpy.ops360.atmservice.logapi.Loggable;
import com.hpy.ops360.atmservice.request.AtmUptimeRequest;
import com.hpy.ops360.atmservice.request.OpenTicketsRequest;
import com.hpy.ops360.atmservice.request.SynergyLoginRequest;
import com.hpy.ops360.atmservice.request.TicketHistoryRequest;
import com.hpy.ops360.atmservice.response.AtmUptimeDetailsResp;
import com.hpy.ops360.atmservice.response.OpenTicketsResponse;
import com.hpy.ops360.atmservice.response.SynergyLoginResponse;
import com.hpy.ops360.atmservice.response.SynergyResponse;

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

	private final RestTemplate restTemplate;

	public SynergyService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Loggable
	public SynergyResponse getSynergyRequestId() {
		String url = synergyBaseUrl + "/trequest";
		return restTemplate.postForObject(url, new SynergyLoginRequest(username, password), SynergyResponse.class);
	}

	@Loggable
	public AtmHistoryNTicketsResponse getntickets(TicketHistoryDto ticketHistoryReqDto) {
		SynergyResponse synergyLoginResponse = new SynergyResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		// API for Get ticket history based on ATM ID and n tickets
		String url = synergyBaseUrl + "/getntickets";

		TicketHistoryRequest ticketHistoryRequest = new TicketHistoryRequest(synergyLoginResponse.getRequestid(),
				ticketHistoryReqDto.getAtmid(), ticketHistoryReqDto.getNticket());
		log.info("SynergyService:getntickets:{}", ticketHistoryRequest);
		AtmHistoryNTicketsResponse atmHistoryNTicketsResponse = restTemplate.postForObject(url, ticketHistoryRequest,
				AtmHistoryNTicketsResponse.class);
		log.info("SynergyService:getntickets:{}", atmHistoryNTicketsResponse);
		return atmHistoryNTicketsResponse;
	}

	@Loggable
	public OpenTicketsResponse getOpenTicketDetails(List<AtmIdDto> atms) {
		SynergyLoginResponse synergyLoginResponse = new SynergyLoginResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		String url = synergyBaseUrl + "/getopentickets";
		log.debug("getopenTicketsUrl: {}", url);
		log.debug("atms: {}", atms.toString());
		OpenTicketsRequest synergyRequest = new OpenTicketsRequest(synergyLoginResponse.getRequestid(), atms);
		log.info("synergyRequest: {}", synergyRequest);

		ResponseEntity<OpenTicketsResponse> responseEntity = restTemplate.postForEntity(url, synergyRequest,
				OpenTicketsResponse.class);
		log.info("OpenTicketsResponse:{}", responseEntity.getBody());
		return responseEntity.getBody();
	}

	@Loggable
	public AtmUptimeDetailsResp getAtmUptime(String atmid) {
		SynergyResponse synergyLoginResponse = new SynergyResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		String url = synergyBaseUrl + "/getatmuptime";

		AtmUptimeRequest atmUptimeRequest = new AtmUptimeRequest(synergyLoginResponse.getRequestid(), atmid);

		return restTemplate.postForObject(url, atmUptimeRequest, AtmUptimeDetailsResp.class);

	}

}
