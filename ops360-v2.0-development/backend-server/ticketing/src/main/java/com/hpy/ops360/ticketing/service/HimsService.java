package com.hpy.ops360.ticketing.service;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.hpy.ops360.ticketing.request.HimsCreateTicketRequest;
import com.hpy.ops360.ticketing.request.HimsUpdateFollowUpDto;
import com.hpy.ops360.ticketing.response.CreateTicketResponse;
import com.hpy.ops360.ticketing.response.HimsCreateTicketResponse;
import com.hpy.ops360.ticketing.response.HimsResponse;
import com.hpy.ops360.ticketing.ticket.dto.CreateTicketHimsDto;

//Custom DTOs

//Logging
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HimsService {


//	@Value("${hims.api.create-ticket:/api/Create_Service_Ticket}")
//	private String himsCreateTicketEndpoint;

	@Value("${hims.connection.timeout:30000}")
	private int connectionTimeout;

	@Value("${hims.read.timeout:30000}")
	private int readTimeout;

	@Value("${hims.enable.dummy:false}")
	private boolean enableDummy;

	@Value("${hims.dummy.url:https://7c18b3a9-3608-415b-b7d6-fb6a3942c1db.mock.pstmn.io/api}")
	private String himsDummyUrl;

	private final RestTemplate restTemplate;
	private final Random random = new Random();

	// Configuration properties
	@Value("${hims.api.base.url:https://7c18b3a9-3608-415b-b7d6-fb6a3942c1db.mock.pstmn.io/api}")
	private String himsBaseUrl;

	@Value("${hims.api.update.endpoint:/Update_FollowUp_Details}")
	private String updateFollowUpEndpoint;
	
	@Value("${hims.api.create.endpoint:/Create_Service_Ticket}")
	private String createTicketEndpoint;
	
	@Value("${hims.api.enabled:false}")
	private boolean himsApiEnabled;

	@Value("${hims.api.timeout:5000}")
	private int timeoutMs;

	@Value("${hims.api.retry.attempts:3}")
	private int retryAttempts;

	@Value("${hims.mock.simulate.delay:true}")
	private boolean simulateDelay;

	@Value("${hims.mock.error.rate:0.1}")
	private double mockErrorRate;

	public HimsService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	 public HimsResponse updateFollowUpDetails(HimsUpdateFollowUpDto updateDto) {
	        log.info("HimsService|updateFollowUpDetails - Starting update for ticket: {}", updateDto.getTicketid());
	        
	        // Validate input
	        if (updateDto == null || updateDto.getTicketid() == null || updateDto.getTicketid().trim().isEmpty()) {
	            log.error("Invalid input: updateDto or ticketid is null/empty");
	            return createErrorResponse("Invalid input parameters");
	        }
	        
	        // Check if real API is enabled
	        if (!himsApiEnabled) {
	            log.info("HIMS API is disabled, using mock response for ticket: {}", updateDto.getTicketid());
	            return createMockResponse(updateDto);
	        }
	        
	        // Try to call real/mock API with retry logic
	        return callApiWithRetry(updateDto);
	    }
	    
	    private HimsResponse callApiWithRetry(HimsUpdateFollowUpDto updateDto) {
	        Exception lastException = null;
	        
	        for (int attempt = 1; attempt <= retryAttempts; attempt++) {
	            try {
	                log.info("HIMS API call attempt {}/{} for ticket: {}", 
	                    attempt, retryAttempts, updateDto.getTicketid());
	                
	                HimsResponse response = makeApiCall(updateDto);
	                
	                if (response != null && "success".equalsIgnoreCase(response.getStatus())) {
	                    log.info("HIMS API call successful on attempt {} for ticket: {}", 
	                        attempt, updateDto.getTicketid());
	                    return response;
	                }
	                
	                log.warn("HIMS API returned non-success status on attempt {}: {}", 
	                    attempt, response != null ? response.getStatus() : "null");
	                
	            } catch (Exception e) {
	                lastException = e;
	                log.error("HIMS API call failed on attempt {}/{} for ticket: {}", 
	                    attempt, retryAttempts, updateDto.getTicketid(), e);
	                
	                // Wait before retry (exponential backoff)
	                if (attempt < retryAttempts) {
	                    try {
	                        Thread.sleep(1000 * attempt); // 1s, 2s, 3s delays
	                    } catch (InterruptedException ie) {
	                        Thread.currentThread().interrupt();
	                        break;
	                    }
	                }
	            }
	        }
	        
	        // All attempts failed, fallback to mock
	        log.error("All HIMS API attempts failed for ticket: {}, falling back to mock response", 
	            updateDto.getTicketid());
	        return createMockResponse(updateDto);
	    }
	    
	    private HimsResponse makeApiCall(HimsUpdateFollowUpDto updateDto) {
	        String url = himsBaseUrl + updateFollowUpEndpoint;
	        log.info("Making HIMS API call to URL: {}", url);
	        
	        // Prepare headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	        
	        // Log request details
	        log.debug("HIMS API Request - URL: {}", url);
	        log.debug("HIMS API Request - Headers: {}", headers);
	        log.debug("HIMS API Request - Body: {}", updateDto);
	        
	        // Create request entity
	        HttpEntity<HimsUpdateFollowUpDto> entity = new HttpEntity<>(updateDto, headers);
	        
	        try {
	            // Make API call
	            ResponseEntity<HimsResponse> responseEntity = restTemplate.exchange(
	                url, 
	                HttpMethod.POST, 
	                entity, 
	                HimsResponse.class
	            );
	            
	            // Log response details
	            log.info("HIMS API Response - Status: {}", responseEntity.getStatusCode());
	            log.debug("HIMS API Response - Headers: {}", responseEntity.getHeaders());
	            log.debug("HIMS API Response - Body: {}", responseEntity.getBody());
	            
	            HimsResponse response = responseEntity.getBody();
	            
	            // Validate response
	            if (response == null) {
	                log.error("HIMS API returned null response body");
	                throw new RuntimeException("Null response from HIMS API");
	            }
	            
	            return response;
	            
	        } catch (RestClientException e) {
	            log.error("RestClientException while calling HIMS API", e);
	            throw new RuntimeException("HIMS API call failed", e);
	        }
	    }
	    
	    private HimsResponse createMockResponse(HimsUpdateFollowUpDto updateDto) {
	        log.info("Creating mock response for ticket: {}", updateDto.getTicketid());
	        
	        // Simulate processing delay if enabled
	        if (simulateDelay) {
	            try {
	                int delay = 100 + random.nextInt(400); // 100-500ms delay
	                Thread.sleep(delay);
	                log.debug("Simulated processing delay of {}ms", delay);
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                log.warn("Mock delay interrupted");
	            }
	        }
	        
	        // Simulate occasional errors for testing
	        if (random.nextDouble() < mockErrorRate) {
	            log.warn("Simulating mock error for ticket: {}", updateDto.getTicketid());
	            return createErrorResponse("Mock error simulation");
	        }
	        
	        // Simulate different responses based on ticket ID patterns
	        String ticketId = updateDto.getTicketid();
	        
	        if (ticketId.contains("INVALID") || ticketId.contains("ERROR")) {
	            log.warn("Mock returning error response for invalid ticket: {}", ticketId);
	            return createErrorResponse("Invalid ticket ID");
	        }
	        
	        // Create successful mock response
	        log.info("Mock returning success response for ticket: {}", ticketId);
	        return HimsResponse.builder()
	            .status("Success")
	            .build();
	    }
	    
	    private HimsResponse createErrorResponse(String errorMessage) {
	        log.error("Creating error response: {}", errorMessage);
	        return HimsResponse.builder()
	            .status("Error")
	            .build();
	    }
	    
	    // Health check method for monitoring
	    public boolean isHimsApiHealthy() {
	        if (!himsApiEnabled) {
	            return true; // Mock is always "healthy"
	        }
	        
	        try {
	            // Create a simple health check request
	            HimsUpdateFollowUpDto healthCheckDto = HimsUpdateFollowUpDto.builder()
	                .ticketid("HEALTH-CHECK")
	                .atmid("HEALTH-CHECK")
	                .etadatetime("01/01/2025 12:00")
	                .owner("SYSTEM")
	                .subcalltype("HEALTH")
	                .customerRemark("Health check")
	                .updatedby("SYSTEM")
	                .comment("Health check")
	                .build();
	            
	            HimsResponse response = makeApiCall(healthCheckDto);
	            return response != null && ("Success".equals(response.getStatus()) || "Error".equals(response.getStatus()));
	            
	        } catch (Exception e) {
	            log.error("HIMS API health check failed", e);
	            return false;
	        }
	    }
	    
	    // Configuration info for debugging
	    public String getConfigurationInfo() {
	        return String.format(
	            "HIMS Service Configuration: enabled=%s, baseUrl=%s, endpoint=%s, timeout=%d, retries=%d",
	            himsApiEnabled, himsBaseUrl, updateFollowUpEndpoint, timeoutMs, retryAttempts
	        );
	    }
	    
	    public HimsCreateTicketResponse createTicket(CreateTicketHimsDto createTicketHimsDto)
	    {	
	    	log.info("Calling method createTicket for HIMS ticket creation: {}",createTicketHimsDto);
	    	String url = himsBaseUrl + createTicketEndpoint;
	    	HimsCreateTicketResponse himsTicketResponse=restTemplate.postForObject(url, createTicketHimsDto, HimsCreateTicketResponse.class);
	    	log.info("HIMS Ticket Creation Response: {}",himsTicketResponse);
	    	return himsTicketResponse;
	    }
}
