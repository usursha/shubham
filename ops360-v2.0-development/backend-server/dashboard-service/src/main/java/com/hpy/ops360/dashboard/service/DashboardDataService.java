package com.hpy.ops360.dashboard.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.hpy.ops360.dashboard.dto.ApiStatusResponse;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DashboardDataService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, Map<String, String>> userApiResponseCache = new ConcurrentHashMap<>();
//    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final List<String> apiUrls = List.of(
        "https://ops360uat.hitachi-payments.com/ops/gateway/sampatti/get-transaction-last-updated-date-time",
        "https://ops360uat.hitachi-payments.com/ops/gateway/dashboard-portal/currentUptime",
        "https://ops360uat.hitachi-payments.com/ops/gateway/dashboard-portal/numberOfCes",
        "https://ops360uat.hitachi-payments.com/ops/gateway/cm-dashboard/counts",
        "https://ops360uat.hitachi-payments.com/ops/gateway/cm-dashboard/getCmSynopsisDetails",
        "https://ops360uat.hitachi-payments.com/ops/gateway/dashboard-portal/activeMachine",
        "https://ops360uat.hitachi-payments.com/ops/gateway/task/list/ce"
    );

    private final LoginService loginService; 

    public DashboardDataService(LoginService loginService) {
        this.loginService = loginService;
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
        String newResponse = fetchDataFromApi(url, bearerToken);

        
        if ("ERROR".equals(newResponse)) {
            log.warn("API call failed for URL: {}", url);
            return new ApiStatusResponse(url, -1, null);
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

        return new ApiStatusResponse(url, status, newResponse);
    }

    private String fetchDataFromApi(String url, String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getBody() != null ? response.getBody() : "EMPTY";
        } catch (Exception e) {
            log.error("Error while calling API: {}", url, e);
            return "ERROR";
        }
    }
}
