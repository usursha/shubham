package com.hpy.mappingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.mappingservice.entity.LeaveRequest;
import com.hpy.mappingservice.repository.UserMasterRepository;
import com.hpy.mappingservice.request.dto.LeaveRequestDTO;
import com.hpy.mappingservice.request.dto.ManagerApproverequest;
import com.hpy.mappingservice.response.dto.LeaveApprovalResponseDto;
import com.hpy.mappingservice.response.dto.LeaveRequestDto;
import com.hpy.mappingservice.response.dto.LeaveSubmissionResponseDto;
import com.hpy.mappingservice.service.LeaveRequestService;
import com.hpy.mappingservice.utils.Helper;
import com.hpy.mappingservice.utils.RestUtilImpl;
import com.hpy.rest.dto.IResponseDto;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/v2/app/api/leave")
public class LeaveRequestController {
	@Autowired
	private LeaveRequestService leaveRequestService;

	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private Helper helper;

	@Autowired
	private RestUtilImpl restUtils;
	

//	@GetMapping("/user-id")
//	public IResponseDto getUserId() {
//		String username = helper.getLoggedInUser();
//		Long id = userMasterRepository.findUserIdByUsername(username);
//		return restUtils.wrapResponse(username, id);
//	}

	@GetMapping("/pending/{managerId}")
	public IResponseDto getPendingRequests() {
		String username = helper.getLoggedInUser();
		Long id = userMasterRepository.findUserIdByUsername(username);
		List<LeaveRequestDto> pendingRequests = leaveRequestService.getPendingRequestsForManager(id);

		return restUtils.wrapResponse(pendingRequests, "fetched Response of pending leave");
	}

	@PostMapping("/submit")
    public IResponseDto submitLeave(@Valid @RequestBody LeaveRequestDTO leaveRequestDto) {
        	try {
        	log.info("Received Leave Request: {}", leaveRequestDto);
    		
            LeaveRequest savedRequest = leaveRequestService.submitLeaveRequest(leaveRequestDto);
            LeaveSubmissionResponseDto responseDto = new LeaveSubmissionResponseDto();
            responseDto.setMessage("Your availability has been successfully shared with your Reporting Manager.");
            responseDto.setRequestId(savedRequest.getId());
            return restUtils.wrapResponse(responseDto,"Leave request submitted successfully");
        	}
            catch (EntityNotFoundException | IllegalArgumentException e) {
            	return restUtils.wrapNullResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
    }

	@PutMapping("/approve/{requestId}")
    public IResponseDto approveLeave(@PathVariable Long requestId,
                                     @RequestBody ManagerApproverequest requestBody) {
        log.info("Received leave approval request. Request ID: {}, Manager ID: {}, Status: {}",
                    requestId, requestBody.getStatus());

        try {
            String status = requestBody.getStatus();

            log.debug("Calling service to update leave request status...");
            leaveRequestService.updateLeaveRequestStatus(requestId, status);
            log.info("Leave request ID {} updated to status '{}' by manager ID {}", requestId, status);

            LeaveApprovalResponseDto responseDto = new LeaveApprovalResponseDto(
                "Leave request " + status + " successfully.",
                requestId,
                status
            );

            log.debug("Wrapping successful response...");
            return restUtils.wrapResponse(responseDto, "Response fetched successfully");

        } catch (EntityNotFoundException e) {
            log.error("Leave request not found. Request ID: {}", requestId, e);
        } catch (SecurityException e) {
            log.warn("Unauthorized attempt to approve leave. Manager ID: {}", e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid input for leave approval. Request ID: {}, Manager ID: {}", requestId, e);
        }

        LeaveApprovalResponseDto errorDto = new LeaveApprovalResponseDto();
        errorDto.setMessage("Error processing leave approval");
        errorDto.setRequestId(requestId);
        errorDto.setStatus("FAILED");

        log.debug("Returning error response...");
        return restUtils.wrapNullResponse("Failed", HttpStatus.BAD_REQUEST);
    }

}