//package com.hpy.ops360.ticketing.route;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import org.apache.camel.Exchange;
//import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.model.dataformat.JsonLibrary;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
//import com.hpy.ops360.ticketing.login.service.LoginService;
//import com.hpy.ops360.ticketing.request.OpenTicketsRequest;
//import com.hpy.ops360.ticketing.request.SynergyLoginRequest;
//import com.hpy.ops360.ticketing.response.OpenTicketsResponse;
//import com.hpy.ops360.ticketing.response.SynergyResponse;
//
//@Component
//public class SynergyApiRoute extends RouteBuilder {
//
//	@Value("${synergy.base-url}")
//	private String synergyBaseUrl;
//
//	@Value("${synergy.username}")
//	private String username;
//
//	@Value("${synergy.password}")
//	private String password;
//
//	private RestTemplate restTemplate;
//
//	// private final LoginService loginService;
//
//	private static final ExecutorService executor = Executors.newFixedThreadPool(10);
//
//	private volatile SynergyResponse cachedSynergyResponse;
//
////	public SynergyService(LoginService loginService) {
//////		this.restTemplate = restTemplate;
////		initiateRestTemplate();
////		this.loginService = loginService;
////	}
//
//	private void initiateRestTemplate() {
//		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//		requestFactory.setConnectTimeout(0); // Set the connection timeout (in milliseconds)
//		requestFactory.setReadTimeout(0); // Set the read timeout (in milliseconds)
//
//		// Create a RestTemplate instance using the custom request factory
//		this.restTemplate = new RestTemplate(requestFactory);
//	}
//
////-------------------added code by shubham---------------------------------------
//
//	public synchronized SynergyResponse getCachedSynergyResponse() {
//		if (cachedSynergyResponse == null || !isSynergyResponseValid(cachedSynergyResponse)) {
//			cachedSynergyResponse = getSynergyRequestId();
//		}
//		return cachedSynergyResponse;
//	}
//
//	public SynergyResponse getSynergyRequestId() {
//		String url = synergyBaseUrl + "/trequest";
//		SynergyResponse response = restTemplate.postForObject(url, new SynergyLoginRequest(username, password),
//				SynergyResponse.class);
//		log.info("Fetched new SynergyResponse: {}", response);
//		return response;
//	}
//
//	public boolean isSynergyResponseValid(SynergyResponse response) {
//		return response != null && "success".equalsIgnoreCase(response.getStatus());
//	}
//
//	public void clearCache() {
//		cachedSynergyResponse = null;
//	}
//
////    @Override
////    public void configure() throws Exception {
////        from("direct:callSynergyApi")
////            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
////            .setHeader("Content-Type", constant("application/json"))
////            .setHeader("Authorization", simple("Basic " + "${type:java.util.Base64}.getEncoder().encodeToString((${synergy.username} + ':' + ${synergy.password}).getBytes())"))
////            .setBody(exchange -> {
////                List<AtmDetailsDto> atms = exchange.getIn().getBody(List.class);
////                SynergyResponse synergyResponse = getCachedSynergyResponse();
////                return new OpenTicketsRequest(synergyResponse.getRequestid(), atms);
////            })
////            .marshal().json(JsonLibrary.Jackson)
////            .to(synergyBaseUrl + "/getopentickets")
////            .unmarshal().json(JsonLibrary.Jackson, OpenTicketsResponse.class);
////    }
//
////	@Override
////	public void configure() throws Exception {
////		from("direct:callSynergyApi").setHeader(Exchange.HTTP_METHOD, constant("POST"))
////				.setHeader("Content-Type", constant("application/json"))
////				.setHeader("Authorization", simple("Basic "
////						+ "${type:java.util.Base64}.getEncoder().encodeToString((${synergy.username} + ':' + ${synergy.password}).getBytes())"))
////				.setBody(exchange -> {
////					List<AtmDetailsDto> atms = exchange.getIn().getBody(List.class);
////					SynergyResponse synergyResponse = getCachedSynergyResponse();
////					return new OpenTicketsRequest(synergyResponse.getRequestid(), atms);
////				}).marshal().json(JsonLibrary.Jackson).to(synergyBaseUrl + "/getopentickets").unmarshal()
////				.json(JsonLibrary.Jackson, OpenTicketsResponse.class);
////	}
//	@Override
//    public void configure() throws Exception {
//        from("direct:callSynergyApi")
//            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
//            .setHeader("Content-Type", constant("application/json"))
//            .setHeader("Authorization", simple("Basic " + "${type:java.util.Base64}.getEncoder().encodeToString((${synergy.username} + ':' + ${synergy.password}).getBytes())"))
//            .setBody(exchange -> {
//                List<AtmDetailsDto> atms = exchange.getIn().getBody(List.class);
//                SynergyResponse synergyResponse = getCachedSynergyResponse();
//                return new OpenTicketsRequest(synergyResponse.getRequestid(), atms);
//            })
//            .marshal().json(JsonLibrary.Jackson)
//            .to(synergyBaseUrl + "/getopentickets")
//            .unmarshal().json(JsonLibrary.Jackson, OpenTicketsResponse.class)
//            .log("Response from Synergy API: ${body}");
//    }
//}