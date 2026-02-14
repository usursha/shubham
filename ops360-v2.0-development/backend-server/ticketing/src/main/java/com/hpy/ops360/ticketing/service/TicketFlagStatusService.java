package com.hpy.ops360.ticketing.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.TicketFlagStatusPinResponseDTO;
import com.hpy.ops360.ticketing.dto.TicketFlagStatusRequestDTO;
import com.hpy.ops360.ticketing.dto.TicketFlagStatusResponseDTO;
import com.hpy.ops360.ticketing.entity.TicketFlagStatus;
import com.hpy.ops360.ticketing.repository.TicketFlagStatusRepository;

@Service
public class TicketFlagStatusService {

	@Autowired
	private TicketFlagStatusRepository ticketFlagStatusRepository;

	public List<TicketFlagStatusPinResponseDTO> getPinTicketsForCeUser(String ceUserId) {
		List<TicketFlagStatus> tickets = ticketFlagStatusRepository.getTicketFlagStatusByCeUserId(ceUserId);

		// Convert Entity to DTO
		return tickets.stream().map(ticket -> new TicketFlagStatusPinResponseDTO(ticket.getCeUserId(),
				ticket.getTicketNumber(), ticket.getFlagStatus())).collect(Collectors.toList());
	}

	public TicketFlagStatusResponseDTO insertOrUpdateFlagStatus(TicketFlagStatusRequestDTO dto) {
		Integer updatedFlagStatus = ticketFlagStatusRepository.insertUpdateFlagStatus(dto.getCeUserId(),
				dto.getTicketNumber(), dto.getFlagStatus());

		TicketFlagStatusResponseDTO responseDTO = new TicketFlagStatusResponseDTO();
		responseDTO.setFlagStatus(updatedFlagStatus != null ? updatedFlagStatus : dto.getFlagStatus());
		return responseDTO;
	}

}
