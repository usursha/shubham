package com.hpy.sampatti_data_service.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hpy.sampatti_data_service.entity.LastUpdatedBankData;
import com.hpy.sampatti_data_service.repository.LastUpdatedBankDataRepository;
import com.hpy.sampatti_data_service.request.DashboardReq;
import com.hpy.sampatti_data_service.request.TokenReq;
import com.hpy.sampatti_data_service.response.ATMDataResp;
import com.hpy.sampatti_data_service.response.DashboardDataResp;
import com.hpy.sampatti_data_service.response.TokenResponse;
import com.hpy.sampatti_data_service.utility.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SampattiService {

	@Value("${sampatti.base-url}")
	private String baseUrl;

	@Value("${sampatti.sessionID}")
	private String sessionId;

	@Value("${sampatti.requestTimestamp}")
	private String requestTimestamp;

	@Value("${sampatti.requestType}")
	private String requestType;

	@Autowired
	private LastUpdatedBankDataRepository lastUpdatedBankDataRepository;
//	@Autowired
//	private RestTemplate restTemplate;

	public TokenResponse getToken(String username) {
//		DisableSslClass.disableSSLVerification();
		String url = baseUrl + "/gettoken";

		RestTemplate restTemplate = null;
		try {
			restTemplate = createRestTemplate();
		} catch (Exception e) {
			e.printStackTrace();
		}

		TokenReq request = new TokenReq(username, username, sessionId, requestTimestamp, requestType);
		return restTemplate.postForObject(url, request, TokenResponse.class);
	}

	public DashboardDataResp getDashboardData(String username) {
		String url = baseUrl + "/getdashborddata";
		String requestType = "GetDashboardData";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String requestTimestamp = LocalDateTime.now().format(formatter);

		RestTemplate restTemplate = null;
		try {
			restTemplate = createRestTemplate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DashboardReq request = new DashboardReq(username, getToken(username).getData(), sessionId, requestTimestamp,
				requestType);
		return restTemplate.postForObject(url, request, DashboardDataResp.class);
	}
	
	
	

	public ATMDataResp getAtmData(String userId) {
	    log.info("Initiating request to fetch ATM data for username: {}", userId);

	    String url = baseUrl + "/atm/transaction/dip";
	    String requestType = "GetTransactionDipAtmData";
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String requestTimestamp = LocalDateTime.now().format(formatter);

	    RestTemplate restTemplate;
	    try {
	        log.debug("Creating RestTemplate for making the API call.");
	        restTemplate = createRestTemplate();
	    } catch (Exception e) {
	        log.error("Failed to create RestTemplate for username: {}", userId, e);
	        throw new RuntimeException("Error initializing RestTemplate", e);
	    }

	    String token;
	    try {
	        log.debug("Fetching token for username: {}", userId);
	        token = getToken(userId).getData();
	        log.debug("Token successfully fetched for username: {}", userId);
	    } catch (Exception e) {
	        log.error("Failed to fetch token for username: {}", userId, e);
	        throw new RuntimeException("Error fetching token", e);
	    }

	    DashboardReq request = new DashboardReq(userId, token, sessionId, requestTimestamp, requestType);
	    log.info("Prepared request payload for username: {}", userId);

	    try {
	        log.debug("Making POST request to URL: {}", url);
	        ATMDataResp response = restTemplate.postForObject(url, request, ATMDataResp.class);
	        log.info("Successfully fetched ATM data for username: {}", userId);
	        return response;
	    } catch (Exception e) {
	        log.error("Error occurred while fetching ATM data for username: {}", userId, e);
	        throw new RuntimeException("Error fetching ATM data", e);
	    }
	}


	private static RestTemplate createRestTemplate() throws Exception {

		// Create a TrustManager that trusts all certificates

		TrustManager[] trustAllCertificates = new TrustManager[] {

				new X509TrustManager() {

					public X509Certificate[] getAcceptedIssuers() {

						return null;

					}

					public void checkClientTrusted(X509Certificate[] certs, String authType) {

					}

					public void checkServerTrusted(X509Certificate[] certs, String authType) {

					}

				}

		};

		// Create SSL context with the trust all certificates TrustManager

		SSLContext sslContext = SSLContext.getInstance("TLS");

		sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

		// Set the default HostnameVerifier to trust all host names

		HostnameVerifier allHostsValid = (hostname, session) -> true;

		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		// Create a RestTemplate with the custom SSL context

		RestTemplate restTemplate = new RestTemplate(new HttpsURLConnectionFactory(sslContext));

		return restTemplate;

	}

	private static class HttpsURLConnectionFactory extends SimpleClientHttpRequestFactory {

		private final SSLContext sslContext;

		public HttpsURLConnectionFactory(SSLContext sslContext) {

			this.sslContext = sslContext;

		}

		@Override
		protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {

			if (connection instanceof HttpsURLConnection) {

				((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());

			}

			super.prepareConnection(connection, httpMethod);

		}

	}

	public DateDto getTransactionLastUpdatedDateTime() throws Exception {
		List<LastUpdatedBankData> list = lastUpdatedBankDataRepository.getBankUpdatedDataFromSp();
		return new DateDto(DateUtil.formatLastUpdatedDateTime(list.get(0).getDate(), list.get(0).getTime()));
	}

}
