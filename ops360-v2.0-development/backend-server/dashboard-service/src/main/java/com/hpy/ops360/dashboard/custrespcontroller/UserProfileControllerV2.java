package com.hpy.ops360.dashboard.custrespcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.dashboard.dto.OrganizationHierarchyListDto;
import com.hpy.ops360.dashboard.dto.PortalUsersLatLongDto;
import com.hpy.ops360.dashboard.service.UserProfileService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v2/reporting-hierarchy")
public class UserProfileControllerV2 {
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private RestUtils restutil;
	
	@PostMapping("/get")
	public ResponseEntity<IResponseDto> getReportingHierarchy() {
		OrganizationHierarchyListDto response=userProfileService.getReportingHierarchy();
		return ResponseEntity.ok(restutil.wrapResponse(response,"success"));
	}
	
	@PutMapping("/update-lat-long")
	public ResponseEntity<IResponseDto> getReportingHierarchy(HttpServletRequest httprequest,@RequestBody @Valid PortalUsersLatLongDto portalUsersLatLongDto) {
		String response=userProfileService.updateLatLong(httprequest, portalUsersLatLongDto);
		return ResponseEntity.ok(restutil.wrapResponse(response,"success"));
	}

}

