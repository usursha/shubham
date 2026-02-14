//package com.hpy.ops360.ticketing.route;
//
//import java.util.List;
//
//import org.apache.camel.ProducerTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.hpy.ops360.ticketing.cm.dto.OpenTicketsWithCategoryDto;
//import com.hpy.ops360.ticketing.config.DisableSslClass;
//import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
//import com.hpy.ops360.ticketing.entity.UserAtmDetails;
//import com.hpy.ops360.ticketing.response.OpenTicketsResponse;
//import com.hpy.ops360.ticketing.service.CmTaskService;
//import com.hpy.ops360.ticketing.service.SynergyService;
//import com.hpy.ops360.ticketing.service.UserAtmDetailsService;
//
//@RestController
//@RequestMapping("/task-1")
//public class TaskRouteController {
//
//	@Autowired
//	private ProducerTemplate producerTemplate;
//
//	@Autowired
//	private CmTaskService taskService;
//
//	@Autowired
//	private SynergyService synergyService;
//	
//	@Autowired
//	private UserAtmDetailsService userAtmDetailsService;
//
//
////	    @GetMapping("/camel/{ceId}/{status}")
////	    public ResponseEntity<String> getCeOpenTicketDetails_save(@PathVariable String ceId) {
////	        //producerTemplate.sendBody("direct:getCeOpenTicketDetails", new Object[]{ceId, status});
////	        producerTemplate.sendBody("direct:getCeOpenTicketDetails", getTicketDetailsByCEAndStatus_save(ceId));
////	        return ResponseEntity.ok("Request processed----------------------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
////	        
////	    }
//
//	@GetMapping("/camel/{ceId}/{status}")
//	public ResponseEntity<OpenTicketsWithCategoryDto> getCeOpenTicketDetails_show(@PathVariable String ceId,
//			@PathVariable String status) {
//		synergyService.clearCache();
//        producerTemplate.sendBody("direct:getCeOpenTicketDetails", getTicketDetailsByCEAndStatus_save(ceId));
//		//producerTemplate.sendBody("direct:getCeOpenTicketDetails", taskService.getTicketDetailsByCEAndStatus(ceId, status));
//		return ResponseEntity.ok(taskService.getTicketDetailsByCEAndStatus(ceId, status));
//	}
//	
//	//--------------------------------------------------------
//	
//	public OpenTicketsResponse getTicketDetailsByCEAndStatus_save(String ceId) {
//		
//		DisableSslClass.disableSSLVerification();
//		List<UserAtmDetails> userAtmDetails = userAtmDetailsService.getUserAtmDetails(ceId); // ATM IDs
//		List<AtmDetailsDto> atmIds = userAtmDetails.stream()
//				.map(atmDetails -> new AtmDetailsDto(atmDetails.getAtm_code())).toList();
//		return synergyService.getOpenTicketDetails(atmIds);
//	}
//}
