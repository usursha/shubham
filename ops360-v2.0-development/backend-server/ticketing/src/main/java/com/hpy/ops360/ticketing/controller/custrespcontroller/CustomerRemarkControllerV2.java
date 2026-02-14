//package com.hpy.ops360.ticketing.controller.custrespcontroller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.hpy.ops360.ticketing.dto.CustomerRemarkDto;
//import com.hpy.ops360.ticketing.service.CustomerRemarkService;
//import com.hpy.ops360.ticketing.ticket.dto.OwnerReqDto;
//import com.hpy.rest.dto.IResponseDto;
//import com.hpy.rest.util.RestUtils;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@RequestMapping("/v2/customer")
//@AllArgsConstructor
//@Slf4j
//@CrossOrigin("${app.cross-origin.allow}")
//public class CustomerRemarkControllerV2 {
//
//	private final CustomerRemarkService customerRemarkService;
//	
//	@Autowired
//	private RestUtils restUtils;
//
//	@PostMapping("/remark/list")
//	public ResponseEntity<IResponseDto> getCustomerRemarks(@RequestBody OwnerReqDto ownerReqDto) {
//	    log.info("***** Inside getCustomerRemarks *****");
//	    log.info("Request Received: " + ownerReqDto);
//	    List<CustomerRemarkDto> result = customerRemarkService.getCutomerRemarks(ownerReqDto.getSubcallType(), ownerReqDto.getBroadCategory());
//	    
//	    log.info("Response Returned from getCustomerRemarks: " + result);
//	    return ResponseEntity.ok(restUtils.wrapResponse(result, "Customer remarks fetched successfully"));
//	}
//
//
//}