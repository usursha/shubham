package com.hpy.ops360.ticketing.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.hpy.ops360.ticketing.cm.dto.AddCommentDto;
import com.hpy.ops360.ticketing.cm.dto.AddCommentRequestDto;
import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
import com.hpy.ops360.ticketing.dto.AtmDetailsWithSourceDto;
import com.hpy.ops360.ticketing.dto.AtmHistoryNTicketsResponse;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.RemarksHistoryDto;
import com.hpy.ops360.ticketing.dto.RemarksrequestDto;
import com.hpy.ops360.ticketing.dto.TicketDetailsDto;
import com.hpy.ops360.ticketing.dto.TicketDetailsReqWithoutReqId;
import com.hpy.ops360.ticketing.dto.TicketHistoryByDateRequest;
import com.hpy.ops360.ticketing.dto.TicketHistoryDto;
import com.hpy.ops360.ticketing.dto.UpdateEtaDto;
import com.hpy.ops360.ticketing.dto.UpdateTicketStatusDto;
import com.hpy.ops360.ticketing.entity.CheckAtmSource;
import com.hpy.ops360.ticketing.entity.MTDUptimeDetails;
import com.hpy.ops360.ticketing.feignclient.request.dto.UpdateSubcallTypeDto;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.CheckAtmSourceRepository;
import com.hpy.ops360.ticketing.repository.MTDUptimeDetailsRepository;
import com.hpy.ops360.ticketing.request.AtmUptimeRequest;
import com.hpy.ops360.ticketing.request.CreateTicketRequest;
import com.hpy.ops360.ticketing.request.OpenTicketsRequest;
import com.hpy.ops360.ticketing.request.SynergyLoginRequest;
import com.hpy.ops360.ticketing.request.TicketDetailsRequest;
import com.hpy.ops360.ticketing.request.TicketHistoryRequest;
import com.hpy.ops360.ticketing.request.UpdateEtaRequest;
import com.hpy.ops360.ticketing.request.UpdateSubcallTypeReq;
import com.hpy.ops360.ticketing.request.UpdateTicketStatusReq;
import com.hpy.ops360.ticketing.response.AtmUptimeDetailsResp;
import com.hpy.ops360.ticketing.response.CreateTicketResponse;
import com.hpy.ops360.ticketing.response.OpenTicketsRequestHims;
import com.hpy.ops360.ticketing.response.OpenTicketsResponse;
import com.hpy.ops360.ticketing.response.SynergyResponse;
import com.hpy.ops360.ticketing.ticket.dto.CreateTicketDto;
import com.hpy.ops360.ticketing.ticket.dto.RemarksResponseDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SynergyService {

	@Autowired
	private ProducerTemplate producerTemplate;

	@Value("${synergy.base-url}")
	private String synergyBaseUrl;

	@Value("${synergy.username}")
	private String username;

	@Value("${synergy.password}")
	private String password;

	private RestTemplate restTemplate;

	private final LoginService loginService;

	private static final ExecutorService executor = Executors.newFixedThreadPool(10);

	private volatile SynergyResponse cachedSynergyResponse;
	
	private CheckAtmSourceRepository checkAtmSourceRepository;
	
	private MTDUptimeDetailsRepository mtdUptimeDetailsRepository;

	public SynergyService(LoginService loginService,CheckAtmSourceRepository checkAtmSourceRepository,MTDUptimeDetailsRepository mtdUptimeDetailsRepository) {
//		this.restTemplate = restTemplate;
		initiateRestTemplate();
		this.loginService = loginService;
		this.checkAtmSourceRepository=checkAtmSourceRepository;
		this.mtdUptimeDetailsRepository = mtdUptimeDetailsRepository;
	}

	private void initiateRestTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(0); // Set the connection timeout (in milliseconds)
		requestFactory.setReadTimeout(0); // Set the read timeout (in milliseconds)

		// Create a RestTemplate instance using the custom request factory
		this.restTemplate = new RestTemplate(requestFactory);
	}

//-------------------added code by shubham---------------------------------------

	public synchronized SynergyResponse getCachedSynergyResponse() {
		if (cachedSynergyResponse == null || !isSynergyResponseValid(cachedSynergyResponse)) {
			cachedSynergyResponse = getSynergyRequestId();
		}
		return cachedSynergyResponse;
	}

	public SynergyResponse getSynergyRequestId() {
		String url = synergyBaseUrl + "/trequest";
		SynergyResponse response = restTemplate.postForObject(url, new SynergyLoginRequest(username, password),
				SynergyResponse.class);
		log.info("Fetched new SynergyResponse: {}", response);
		return response;
	}

	public boolean isSynergyResponseValid(SynergyResponse response) {
		return response != null && "success".equalsIgnoreCase(response.getStatus());
	}

	public void clearCache() {
		cachedSynergyResponse = null;
	}

	public AtmHistoryNTicketsResponse getntickets(TicketHistoryDto ticketHistoryReqDto) {
		SynergyResponse synergyLoginResponse = new SynergyResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		// API for Get ticket history based on ATM ID and n tickets
		String url = synergyBaseUrl + "/getntickets";

		TicketHistoryRequest ticketHistoryRequest = new TicketHistoryRequest(synergyLoginResponse.getRequestid(),
				ticketHistoryReqDto.getAtmid(), ticketHistoryReqDto.getNticket());
		log.info("SynergyService:getntickets Request:{}", ticketHistoryRequest);
		AtmHistoryNTicketsResponse atmHistoryNTicketsResponse = restTemplate.postForObject(url, ticketHistoryRequest,
				AtmHistoryNTicketsResponse.class);
		log.info("SynergyService:getntickets Response:{}", atmHistoryNTicketsResponse);
		return atmHistoryNTicketsResponse;
	}

	public TicketDetailsDto getTicketDetails(TicketDetailsReqWithoutReqId ticketDetailsRequest) {
		SynergyResponse synergyLoginResponse = new SynergyResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		// API for Get ticket details
		String url = synergyBaseUrl + "/getticketdetails";

		TicketDetailsRequest ticketRequest = new TicketDetailsRequest(synergyLoginResponse.getRequestid(),
				ticketDetailsRequest.getTicketno(), ticketDetailsRequest.getAtmid());
		log.info("ticket details synticketRequest:{}", ticketRequest);
		TicketDetailsDto ticketDetailsDto = restTemplate.postForObject(url, ticketRequest, TicketDetailsDto.class);
		log.info("ticket details synticketResponse:{}", ticketDetailsDto);
		return ticketDetailsDto;
	}

	public AtmUptimeDetailsResp getAtmUptime(String atmid) {
	    if (atmid == null || atmid.trim().isEmpty()) {
	        log.error("ATM ID is null or empty. Please provide a valid ATM ID.");
	        return new AtmUptimeDetailsResp("", atmid, 0.0, 0.0);
	    }

	    List<CheckAtmSource> atms = checkAtmSourceRepository.checkAtmSource(atmid);
	    if (atms == null || atms.isEmpty()) {
	        log.warn("No ATM source found for ATM ID: {}", atmid);
	        return new AtmUptimeDetailsResp("", atmid, 0.0, 0.0);
	    }

	    log.info("ATM source response for {}: {}", atmid, atms);

	    CheckAtmSource sourceInfo = atms.get(0);
	    int source = sourceInfo.getSourceCode();

	    if (source == 0) { // synergy
	        SynergyResponse synergyRequestId = getSynergyRequestId();
	        if (synergyRequestId == null) {
	            log.error("Synergy request ID is null for ATM ID: {}", atmid);
	            return new AtmUptimeDetailsResp("", atmid, 0.0, 0.0);
	        }

	        SynergyResponse synergyLoginResponse = new SynergyResponse(
	            synergyRequestId.getRequestid(),
	            synergyRequestId.getStatus()
	        );

	        String url = synergyBaseUrl + "/getatmuptime";
	        AtmUptimeRequest atmUptimeRequest = new AtmUptimeRequest(synergyLoginResponse.getRequestid(), atmid);

	        try {
	            AtmUptimeDetailsResp response = restTemplate.postForObject(url, atmUptimeRequest, AtmUptimeDetailsResp.class);
	            if (response == null) {
	                log.warn("Synergy API returned null response for ATM ID: {}", atmid);
	                return new AtmUptimeDetailsResp("", atmid, 0.0, 0.0);
	            }
	            return response;
	        } catch (Exception e) {
	            log.error("Error while calling Synergy API for ATM ID: {}", atmid, e);
	            return new AtmUptimeDetailsResp("", atmid, 0.0, 0.0);
	        }
	    }

	    MTDUptimeDetails mtdUptimeDetails = mtdUptimeDetailsRepository.getAtmUptime(atmid);
	    if (mtdUptimeDetails == null) {
	        log.warn("MTD uptime details not found for ATM ID: {}", atmid);
	        return new AtmUptimeDetailsResp("", atmid, 0.0, 0.0);
	    }

	    return new AtmUptimeDetailsResp(
	        String.valueOf(mtdUptimeDetails.getSrno()),
	        mtdUptimeDetails.getAtmid(),
	        mtdUptimeDetails.getMtd(),
	        mtdUptimeDetails.getLastmonthuptime()
	    );
	}


	public CreateTicketResponse createTicket(CreateTicketDto ticket) {
		SynergyResponse synergyLoginResponse = new SynergyResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());
		String url = synergyBaseUrl + "/createticket";
		CreateTicketRequest createTicket = new CreateTicketRequest(synergyLoginResponse.getRequestid(),
				ticket.getAtmid(), ticket.getRefno(), ticket.getDiagnosis(), ticket.getCreatedby(),
				ticket.getDocumentname(), ticket.getDocument(), ticket.getComment());
		log.info("Synergy createTicketReq: {}", createTicket);
		CreateTicketResponse createTicketResponse = restTemplate.postForObject(url, createTicket,
				CreateTicketResponse.class);
		log.info("Synergy createTicketResp: {}", createTicketResponse);
		return createTicketResponse;
	}

	public SynergyResponse updateEta(UpdateEtaDto updateEtaDto) {
		SynergyResponse synergyLoginResponse = new SynergyResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());
		String url = synergyBaseUrl + "/updateturnaroundtime";
		UpdateEtaRequest updateEtaRequest = new UpdateEtaRequest(synergyLoginResponse.getRequestid(),
				updateEtaDto.getTicketid(), updateEtaDto.getAtmid(), updateEtaDto.getEtadatetime(),
				loginService.getLoggedInUser(), updateEtaDto.getComment());
		log.info("Synergy updateEtaReq: {}", updateEtaRequest);
		SynergyResponse resp = restTemplate.postForObject(url, updateEtaRequest, SynergyResponse.class);
		log.info("SynergyResponse for update eta: {}", resp);
		return resp;
	}

	public RemarksResponseDto getRemarksHistory(RemarksrequestDto requestsdto) {
		log.info("getRemarksHistory| requestsdto:{}", requestsdto);
		SynergyResponse synergyLoginResponse = new SynergyResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());
		String url = synergyBaseUrl + "/gettickethistory";
		RemarksHistoryDto request = new RemarksHistoryDto(synergyLoginResponse.getRequestid(),
				requestsdto.getTicketno(), requestsdto.getAtmid());
		log.info("getRemarksHistory| synRequest:{}", request);
		RemarksResponseDto remarksResponseDto = restTemplate.postForObject(url, request, RemarksResponseDto.class);
		log.info("getRemarksHistory| synResponse:{}", remarksResponseDto);
		return remarksResponseDto;
	}

	@Retryable(value = { HttpServerErrorException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
	public OpenTicketsResponse getOpenTicketDetails(List<AtmDetailsDto> atms) {
		SynergyResponse synergyResponse = getCachedSynergyResponse();

		String url = synergyBaseUrl + "/getopentickets";
		log.debug("getopenTicketsUrl: {}", url);
		log.debug("atms: {}", atms);

		OpenTicketsRequest synergyRequest = new OpenTicketsRequest(synergyResponse.getRequestid(), atms);
		log.info("synergyRequest: {}", synergyRequest);

		return CompletableFuture.supplyAsync(() -> {
			try {
				ResponseEntity<OpenTicketsResponse> responseEntity = restTemplate.postForEntity(url, synergyRequest,
						OpenTicketsResponse.class);
				OpenTicketsResponse response = responseEntity.getBody();
				producerTemplate.sendBody("direct:getCeOpenTicketDetails", response);

				log.info("OpenTicketsResponse: {}", response);
				return response;
			} catch (HttpClientErrorException e) {
				log.error("HTTP error occurred while calling Synergy API: {}", e.getRawStatusCode(), e);
				throw new RuntimeException("Failed to fetch open tickets from Synergy API", e);
			} catch (RestClientException e) {
				log.error("Rest client exception occurred: {}", e.getMessage(), e);
				throw new RuntimeException("Failed to communicate with Synergy API", e);
			}
		}, executor).exceptionally(ex -> {
			log.error("Exception occurred while fetching open tickets: {}", ex.getMessage(), ex);
			throw new RuntimeException("Failed to fetch open tickets", ex);
		}).join();
	}
	
	
	//for hmis 
	
	@Retryable(value = { HttpServerErrorException.class }, maxAttempts = 3, backoff = @Backoff(delay = 2000))
	public OpenTicketsResponse getOpenTicketDetailsHims(List<AtmDetailsWithSourceDto> atms) {
		SynergyResponse synergyResponse = getCachedSynergyResponse();
		
		String url = synergyBaseUrl + "/getopentickets";
		log.debug("getopenTicketsUrl: {}", url);
		log.debug("atms: {}", atms);
		
		OpenTicketsRequestHims synergyRequest = new OpenTicketsRequestHims(synergyResponse.getRequestid(), atms);
		log.info("synergyRequest: {}", synergyRequest);
		
		return CompletableFuture.supplyAsync(() -> {
			try {
				ResponseEntity<OpenTicketsResponse> responseEntity = restTemplate.postForEntity(url, synergyRequest,
						OpenTicketsResponse.class);
				OpenTicketsResponse response = responseEntity.getBody();
				producerTemplate.sendBody("direct:getCeOpenTicketDetails", response);
				
				log.info("OpenTicketsResponse: {}", response);
				return response;
			} catch (HttpClientErrorException e) {
				log.error("HTTP error occurred while calling Synergy API: {}", e.getRawStatusCode(), e);
				throw new RuntimeException("Failed to fetch open tickets from Synergy API", e);
			} catch (RestClientException e) {
				log.error("Rest client exception occurred: {}", e.getMessage(), e);
				throw new RuntimeException("Failed to communicate with Synergy API", e);
			}
		}, executor).exceptionally(ex -> {
			log.error("Exception occurred while fetching open tickets: {}", ex.getMessage(), ex);
			throw new RuntimeException("Failed to fetch open tickets", ex);
		}).join();
	}

	// ---------------------------------
	public GenericResponseDto addComments(AddCommentDto addCommentDto) {

		SynergyResponse synergyLoginResponse = new SynergyResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		String url = synergyBaseUrl + "/addcomments";

		AddCommentRequestDto addCommentRequestDto = new AddCommentRequestDto();
		addCommentRequestDto.setAtmId(addCommentDto.getAtmId());
		addCommentRequestDto.setTicketNo(addCommentDto.getTicketNo());
		addCommentRequestDto.setComments(addCommentDto.getComments());
		addCommentRequestDto.setUserId(loginService.getLoggedInUser());
		addCommentRequestDto.setRequestId(synergyLoginResponse.getRequestid());
		ResponseEntity<SynergyResponse> responseEntity = restTemplate.postForEntity(url, addCommentRequestDto,
				SynergyResponse.class);

		if (responseEntity.getBody().getStatus().equalsIgnoreCase("success")) {
			return new GenericResponseDto(responseEntity.getBody().getStatus(), "comment added ");
		}
		return new GenericResponseDto(responseEntity.getBody().getStatus(), "Invalid Request Id");

	}

	public GenericResponseDto updateTicketStatus(UpdateTicketStatusDto updateTicketStatusDto) {
		log.info("updateTicketStatus|updateTicketStatusDto:{}", updateTicketStatusDto);
		SynergyResponse synergyLoginResponse = new SynergyResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		String url = synergyBaseUrl + "/updateticketstatus";

		UpdateTicketStatusReq updateTicketStatus = UpdateTicketStatusReq.builder()
				.requestid(synergyLoginResponse.getRequestid()).atmId(updateTicketStatusDto.getAtmId())
				.ticketNo(updateTicketStatusDto.getTicketNo()).comment(updateTicketStatusDto.getComment())
				.document(updateTicketStatusDto.getDocument()).documentName(updateTicketStatusDto.getDocumentName())
				.updatedBy(updateTicketStatusDto.getUpdatedby()).build();

		log.info("updateTicketStatus|updateTicketStatusReq:{}", updateTicketStatus);
		SynergyResponse response = restTemplate.postForObject(url, updateTicketStatus, SynergyResponse.class);

		if (response.getStatus().equalsIgnoreCase("success")) {
			return new GenericResponseDto(response.getStatus(), "comment added with image ");
		}
		return new GenericResponseDto(response.getStatus(), "Invalid Request Id");

	}

	public SynergyResponse updateSubcallType(UpdateSubcallTypeDto updateSubcallTypeDto) {
		log.info("updateSubcallType|updateSubcallTypeDto:{}", updateSubcallTypeDto);
		SynergyResponse synergyLoginResponse = new SynergyResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());
		String url = synergyBaseUrl + "/updatesubcalltype";

		UpdateSubcallTypeReq request = UpdateSubcallTypeReq.builder().requestid(synergyLoginResponse.getRequestid())
				.ticketNo(updateSubcallTypeDto.getTicketNo()).atmId(updateSubcallTypeDto.getAtmId())
				.subcallType(updateSubcallTypeDto.getSubcallType()).comments(updateSubcallTypeDto.getComments())
				.updatedBy(updateSubcallTypeDto.getUpdatedBy()).build();
		SynergyResponse response = restTemplate.postForObject(url, request, SynergyResponse.class);
		log.info("updateSubcallType|response:{}", response);
		return response;
	}

	public AtmHistoryNTicketsResponse getTicketByDateHistory(String atmId, String fromDate) {
		SynergyResponse synergyLoginResponse = new SynergyResponse(getSynergyRequestId().getRequestid(),
				getSynergyRequestId().getStatus());

		// API for Get ticket history based on ATM ID and n tickets
		String url = synergyBaseUrl + "/gettickethistorydate";

		TicketHistoryByDateRequest ticketHistoryRequest = new TicketHistoryByDateRequest(
				synergyLoginResponse.getRequestid(), atmId, fromDate);
		log.info("SynergyService:getTicketByDateHistory Request:{}", ticketHistoryRequest);
		AtmHistoryNTicketsResponse atmHistoryTicketsResponse = restTemplate.postForObject(url, ticketHistoryRequest,
				AtmHistoryNTicketsResponse.class);
		log.info("SynergyService:getTicketByDateHistory Response:{}", atmHistoryTicketsResponse);
		return atmHistoryTicketsResponse;
	}

}
