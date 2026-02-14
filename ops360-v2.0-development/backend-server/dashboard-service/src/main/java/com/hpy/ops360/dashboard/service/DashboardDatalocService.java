package com.hpy.ops360.dashboard.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hpy.ops360.dashboard.dto.ApiStatusResponse;
import com.hpy.ops360.dashboard.dto.TransactionDetailsRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashboardDatalocService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, Map<String, String>> userApiResponseCache = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final List<String> apiUrls;
    private LoginService loginService; 

    public DashboardDatalocService(LoginService loginService, DashboardConfig dashboardConfig) {
        this.loginService = loginService;
        this.apiUrls = dashboardConfig.getApiUrls();
    }
    

    public List<ApiStatusResponse> fetchAndCompareData(String bearerToken) {
        String username = loginService.getLoggedInUser();
        log.info("User: {}", username);

        // Ensure a user-specific cache exists
        userApiResponseCache.putIfAbsent(username, new ConcurrentHashMap<>());
        Map<String, String> userCache = userApiResponseCache.get(username);

        // Execute API calls in parallel
        List<CompletableFuture<ApiStatusResponse>> futures = apiUrls.stream()
            .map(url -> CompletableFuture.supplyAsync(() -> fetchAndCompare(url, bearerToken, userCache, username), executorService))
            .collect(Collectors.toList());

        // Wait for all futures to complete and collect results
        return futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

    private ApiStatusResponse fetchAndCompare(String url, String bearerToken, Map<String, String> userCache, String username) {
        log.info("Fetching data from API: {}", url);
        String newResponse = fetchDataFromApi(url, bearerToken, username);

        
        if ("ERROR".equals(newResponse)) {
            log.warn("API call failed for URL: {}", url);
            return new ApiStatusResponse(getEndpoint(url), -1, null);
        }

        String oldResponse = userCache.get(url);
        log.info("Old Response fetched from Cache: {}", oldResponse);
        log.info("New Response fetched from API: {}", newResponse);

        int status = (oldResponse == null || !oldResponse.equals(newResponse)) ? 1 : 0;

        if (status == 1) {
            log.info("Data changed for API: {}. Updating cache for user {}", url, username);
            userCache.put(url, newResponse);
        } else {
            log.info("No change detected for API: {}", url);
        }

        return new ApiStatusResponse(getEndpoint(url), status, newResponse);
    }
    
    
    private String fetchDataFromApi(String url, String bearerToken,String username) {
    	log.info("*** Inside Fetch data from API ***");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Check if the URL is for "get_trans_target" and send POST request with userId
        if (url.contains("/ticket/get_trans_target")) {
            log.info("*** Inside IF Condition ***");

            TransactionDetailsRequest requestBody = new TransactionDetailsRequest(username); 
            log.info("User given: " + username);

            HttpEntity<TransactionDetailsRequest> entity = new HttpEntity<>(requestBody, headers);
            return sendpostRequest(url, HttpMethod.POST, entity);
        } else {
        	HttpEntity<String> entity = new HttpEntity<>(headers);
            return sendRequest(url, HttpMethod.GET, entity);
        }
    }

    
    private String sendpostRequest(String url, HttpMethod method, HttpEntity<TransactionDetailsRequest> entity) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
            return response.getBody() != null ? response.getBody() : "EMPTY";
        } catch (Exception e) {
            log.error("Error while calling API: {}", url, e);
            return "ERROR";
        }
    }
    
    private String sendRequest(String url, HttpMethod method, HttpEntity<String> entity) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, method, entity, String.class);
            return response.getBody() != null ? response.getBody() : "EMPTY";
        } catch (Exception e) {
            log.error("Error while calling API: {}", url, e);
            return "ERROR";
        }
    }
    
    private String getEndpoint(String url) {
        try {
            return new java.net.URL(url).getPath();
        } catch (Exception e) {
            log.error("Error extracting endpoint from URL: {}", url, e);
            return url; // Fallback to full URL if parsing fails
        }
    }
}
