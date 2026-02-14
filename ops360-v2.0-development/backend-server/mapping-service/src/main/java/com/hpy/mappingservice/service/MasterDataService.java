package com.hpy.mappingservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.AbsenceSlot;
import com.hpy.mappingservice.entity.LeaveType;
import com.hpy.mappingservice.entity.Reason;
import com.hpy.mappingservice.repository.AbsenceSlotRepository;
import com.hpy.mappingservice.repository.LeaveTypeRepository;
import com.hpy.mappingservice.repository.ReasonRepository;
import com.hpy.mappingservice.response.dto.AbsenceSlotDTO;
import com.hpy.mappingservice.response.dto.LeaveTypeDTO;
import com.hpy.mappingservice.response.dto.ReasonDTO;

@Service
public class MasterDataService {
	@Autowired
	private LeaveTypeRepository leaveTypeRepository;
	@Autowired
	private ReasonRepository reasonRepository;
	@Autowired
	private AbsenceSlotRepository absenceSlotRepository;

	public List<LeaveTypeDTO> getAllLeaveTypes() {

		List<LeaveType> data = leaveTypeRepository.findByIsActiveTrue();
		List<LeaveTypeDTO> response = data.stream()
				.map(result -> new LeaveTypeDTO(result.getSrno(), result.getName(), result.isActive()

				)).toList();

		return response;
	}

	public List<ReasonDTO> getAllReasons() {
		List<Reason> data = reasonRepository.findByIsActiveTrue();
		List<ReasonDTO> response = data.stream()
				.map(result -> new ReasonDTO(result.getSrno(), result.getName(), result.isActive()

				)).toList();
		return response;
	}

	public List<AbsenceSlotDTO> getAllAbsenceSlots() {
		List<AbsenceSlot> data = absenceSlotRepository.findByIsActiveTrue();

		List<AbsenceSlotDTO> response = data.stream().map(result -> new AbsenceSlotDTO(result.getSrno(),
				result.getName(), result.getStartTime(), result.getEndTime(), result.isActive()

		)).toList();
		return response;
	}
}