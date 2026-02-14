package com.hpy.mappingservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.AbsenceSlot;
import com.hpy.mappingservice.entity.LeaveRequest;
import com.hpy.mappingservice.entity.LeaveRequestentity;
import com.hpy.mappingservice.entity.LeaveType;
import com.hpy.mappingservice.entity.Reason;
import com.hpy.mappingservice.entity.UserMapping;
import com.hpy.mappingservice.repository.AbsenceSlotRepository;
import com.hpy.mappingservice.repository.LeaveRequestRepository;
import com.hpy.mappingservice.repository.LeaveRequestdataRepository;
import com.hpy.mappingservice.repository.LeaveTypeRepository;
import com.hpy.mappingservice.repository.ReasonRepository;
import com.hpy.mappingservice.repository.UserMasterRepository;
import com.hpy.mappingservice.repository.UserRepository;
import com.hpy.mappingservice.request.dto.LeaveRequestDTO;
import com.hpy.mappingservice.response.dto.LeaveRequestDto;
import com.hpy.mappingservice.utils.Helper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeaveRequestService {

    @Autowired 
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired 
    private UserRepository userRepository;
    @Autowired 
    private LeaveTypeRepository leaveTypeRepository;
    @Autowired 
    private ReasonRepository reasonRepository;
    @Autowired 
    private AbsenceSlotRepository absenceSlotRepository;
    
	@Autowired
	private UserMasterRepository userMasterRepository;

	@Autowired
	private Helper helper;
    
    @Autowired
    private LeaveRequestdataRepository leaveRequestdataRepository;
    
    public List<LeaveRequestDto> getPendingRequestsForManager(Long managerId) {
    	List<LeaveRequestentity> response = leaveRequestdataRepository.findByManagerUsersAndStatus(managerId, "Pending");
    	List<LeaveRequestDto> data=response.stream().map(result -> new LeaveRequestDto(
    			result.getSrno(),
    			result.getUserid(),
    			result.getRequestDate(),
    			result.getLeaveTypeid(),
    			result.getReason(),
    			result.getAbsenceSlot(),
    			result.getCustomStartTime(),
    			result.getCustomEndTime(),
    			result.getRemarks(),
    			result.getStatus(),
    			result.getCreatedAt()
    			)).toList();
    			
        return data;
    }

    @Transactional
    public LeaveRequest submitLeaveRequest(LeaveRequestDTO dto) {
    	
    	String username = helper.getLoggedInUser();
		Long id = userMasterRepository.findUserIdByUsername(username);
        log.info("Starting leave request submission for User ID: {}", id);

        
        UserMapping user = userRepository.findByUserId(id)
        	    .orElseThrow(() -> {
        	        log.error("User not found with ID: {}", id);
        	        return new EntityNotFoundException("User not found with ID: " + id);
        	    });

        log.info("User found: {}", user.getUser());

        LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> {
                    log.error("Leave Type not found with ID: {}", dto.getLeaveTypeId());
                    return new EntityNotFoundException("Leave Type not found with ID: " + dto.getLeaveTypeId());
                });
        log.info("Leave Type retrieved: {}", leaveType.getName());

        Reason reason = reasonRepository.findById(dto.getReasonId())
                .orElseThrow(() -> {
                    log.error("Reason not found with ID: {}", dto.getReasonId());
                    return new EntityNotFoundException("Reason not found with ID: " + dto.getReasonId());
                });
        log.info("Reason retrieved: {}", reason.getName());

        AbsenceSlot absenceSlot = absenceSlotRepository.findById(dto.getAbsenceSlotId())
                .orElseThrow(() -> {
                    log.error("Absence Slot not found with ID: {}", dto.getAbsenceSlotId());
                    return new EntityNotFoundException("Absence Slot not found with ID: " + dto.getAbsenceSlotId());
                });
        log.info("Absence Slot retrieved: {}", absenceSlot.getName());

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setUser(user);
        leaveRequest.setRequestDate(LocalDateTime.now());
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setReason(reason);
        leaveRequest.setAbsenceSlot(absenceSlot);
        leaveRequest.setRemarks(dto.getRemarks());
        leaveRequest.setStatus("Pending");

        log.info("Processing custom time logic for Absence Slot ID: {}", dto.getAbsenceSlotId());

        LocalDateTime customStart;
        LocalDateTime customEnd;

        switch (dto.getAbsenceSlotId()) {
            case 6:
                log.info("Custom slot detected. Using provided custom start time: {}", dto.getCustomStartTime());
                LocalDate customDate = dto.getCustomStartTime().toLocalDate();
                customStart = LocalDateTime.of(customDate, absenceSlot.getStartTime());
                customEnd = LocalDateTime.of(customDate, absenceSlot.getEndTime());
                break;

            case 3:
                log.info("Overnight slot detected. Calculating start and end across two days.");
                LocalDate today = LocalDate.now();
                LocalDate tomorrow = today.plusDays(1);
                customStart = LocalDateTime.of(today, absenceSlot.getStartTime());
                customEnd = LocalDateTime.of(tomorrow, absenceSlot.getEndTime());
                break;

            case 4:
                log.info("Full day slot detected. Calculating start and end across two days.");
                LocalDate fullDayStart = LocalDate.now();
                LocalDate fullDayEnd = fullDayStart.plusDays(1);
                customStart = LocalDateTime.of(fullDayStart, absenceSlot.getStartTime());
                customEnd = LocalDateTime.of(fullDayEnd, absenceSlot.getEndTime());
                break;

            default:
                log.info("Standard slot detected. Using today's date for start and end.");
                LocalDate standardDate = LocalDate.now();
                customStart = LocalDateTime.of(standardDate, absenceSlot.getStartTime());
                customEnd = LocalDateTime.of(standardDate, absenceSlot.getEndTime());
                break;
        }

        log.info("Custom Start Time set to: {}", customStart);
        log.info("Custom End Time set to: {}", customEnd);

        leaveRequest.setCustomStartTime(customStart);
        leaveRequest.setCustomEndTime(customEnd);

        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        log.info("Leave request saved successfully with ID: {}", savedRequest.getId());

        log.info("Notifying manager (ID: {}) about new leave request for user: {}", user.getManager(), user.getUser());

        return savedRequest;
    }

    

    @Transactional
    public LeaveRequest updateLeaveRequestStatus(Long requestId, String status) {
    	String username = helper.getLoggedInUser();
		Long managerId = userMasterRepository.findUserIdByUsername(username);
        log.info("Attempting to update leave request. Request ID: {}, Manager ID: {}, Status: {}", requestId, managerId, status);

        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Leave Request not found with ID: {}", requestId);
                    return new EntityNotFoundException("Leave Request not found with ID: " + requestId);
                });

        if (!request.getUser().getManager().equals(managerId)) {
            log.warn("Unauthorized access attempt by Manager ID: {} for Request ID: {}", managerId, requestId);
            throw new SecurityException("You are not authorized to approve this request.");
        }

        if (!"Approved".equalsIgnoreCase(status) && !"Rejected".equalsIgnoreCase(status)) {
            log.error("Invalid status '{}' provided for Request ID: {}", status, requestId);
            throw new IllegalArgumentException("Invalid status provided. Must be 'Approved' or 'Rejected'.");
        }

        request.setStatus(status);
        log.info("Leave request ID {} has been {} by Manager ID {}", requestId, status, managerId);

        LeaveRequest updatedRequest = leaveRequestRepository.save(request);
        log.debug("Leave request ID {} successfully saved with new status '{}'", requestId, status);

        return updatedRequest;
    }
}