//package com.hpy.ops360.ticketing.fiegnclient;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.core.io.Resource;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
////@FeignClient(name = "dashboard-service", url = "${dashboard-service.url}")
//@FeignClient(name="dashboard-service", url="${dashboard-service.url}")
//public interface CMPortalAtmDetailsFiegnClient {
//    @GetMapping("/download-csv/{userId}")
//    ResponseEntity<Resource> downloadCsv(@PathVariable("userId") String userId);
//
//    @GetMapping("/download-excel/{userId}")
//    ResponseEntity<String> downloadExcel(@PathVariable("userId") String userId);
//}
