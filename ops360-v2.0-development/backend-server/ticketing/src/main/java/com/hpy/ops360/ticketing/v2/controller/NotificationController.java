package com.hpy.ops360.ticketing.v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.cm.dto.NotificationFilterRequest;
import com.hpy.ops360.ticketing.request.NotificationSearch;
import com.hpy.ops360.ticketing.request.NotificationSearchResponse;
import com.hpy.ops360.ticketing.response.FilterOptionsResponse;
import com.hpy.ops360.ticketing.response.NotificationPaginatedResponse;
import com.hpy.ops360.ticketing.service.TaskService;
import com.hpy.ops360.ticketing.ticket.dto.NotificationCountDTO;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v2/notifications")
public class NotificationController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private RestUtils restUtils;

	@GetMapping
	public ResponseEntity<IResponseDto> getManagerTeamTasks(@RequestParam(required = false) String type,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		NotificationPaginatedResponse managerCombinedTeamData = taskService.getManagerCombinedTeamData(page, size,
				type);

		return ResponseEntity.ok(restUtils.wrapResponse(managerCombinedTeamData, "success"));
	}

	@GetMapping("/count")
	public ResponseEntity<IResponseDto> getNotificationCount(@RequestParam(required = false) String type) {
		NotificationCountDTO notificationCount = taskService.getNotificationCount(type);
		return ResponseEntity.ok(restUtils.wrapResponse(notificationCount, "success"));
	}

	@GetMapping("/filter")
	public ResponseEntity<IResponseDto> getFilterOptionsResponse() {
		FilterOptionsResponse filterOptionsResponse = taskService.getFilterOptionsResponse();
		return ResponseEntity.ok(restUtils.wrapResponse(filterOptionsResponse, "success"));
	}

//	changed the method type from get to post
	@PostMapping("/search")
	public ResponseEntity<IResponseDto> searchRecords(@Valid @RequestBody NotificationSearch notificationSearch) {
		NotificationSearchResponse managerCombinedTeamDataWithSearch = taskService
				.getManagerCombinedTeamDataWithSearch(notificationSearch);
		return ResponseEntity
				.ok(restUtils.wrapResponseListOfString(managerCombinedTeamDataWithSearch.getResponse(), "success"));
	}

	@PostMapping("/filterNotification")
	public ResponseEntity<IResponseDto> getManagerCombinedTeamData(
			@Valid @RequestBody NotificationFilterRequest notificationFilterRequest) {
		NotificationPaginatedResponse managerCombinedTeamData = taskService
				.getManagerCombinedTeamData(notificationFilterRequest);
		return ResponseEntity.ok(restUtils.wrapResponse(managerCombinedTeamData, "success"));
	}

}
