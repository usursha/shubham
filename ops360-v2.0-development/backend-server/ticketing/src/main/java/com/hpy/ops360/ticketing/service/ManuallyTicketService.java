package com.hpy.ops360.ticketing.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.GetManuallyTicketResponseDTO;
import com.hpy.ops360.ticketing.dto.GroupedTicketResponse;
import com.hpy.ops360.ticketing.entity.ManuallyTickets;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.ManuallyTicketRepository;

@Service
public class ManuallyTicketService {

    @Autowired
    private ManuallyTicketRepository repository;

    @Autowired
    private LoginService loginService;
	public List<GetManuallyTicketResponseDTO> getManuallyTickets() {
		String username =loginService.getLoggedInUser();
		List<ManuallyTickets> tickets = repository.getManuallyTickets(username);
		
		return tickets.stream().map(this::convertToDTO).collect(Collectors.toList());
	}
	
	 public List<GroupedTicketResponse> getGroupedManuallyTickets() {
	        String username = loginService.getLoggedInUser();
	        List<ManuallyTickets> tickets = repository.getManuallyTickets(username);

//	        int total_records=tickets.size();
	        // Group tickets by createdDate (day) and format the dates
	        Map<LocalDate, List<GetManuallyTicketResponseDTO>> groupedTickets = tickets.stream()
	            .map(this::convertToDTO)
	            .collect(Collectors.groupingBy(dto -> dto.getCreatedDate().toLocalDate()));

	        return groupedTickets.entrySet().stream()
	                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))  // Sort groups by date in descending order
	                .map(entry -> {
	                    // Sort each group by `createdDate` in descending order
	                    List<GetManuallyTicketResponseDTO> sortedTickets = entry.getValue().stream()
	                        .sorted(Comparator.comparing(GetManuallyTicketResponseDTO::getCreatedDate).reversed())
	                        .collect(Collectors.toList());

	                    return new GroupedTicketResponse(formatDate(entry.getKey()), sortedTickets);
	                })
	                .collect(Collectors.toList());
	    }


	 private String formatDate(LocalDate date) {
	        LocalDate today = LocalDate.now();
	        LocalDate yesterday = today.minusDays(1);

	        if (date.equals(today)) {
	            return "Today | " + today.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy"));
	        } else if (date.equals(yesterday)) {
	            return "Yesterday | " + yesterday.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy"));
	        } else {
	            return date.format(DateTimeFormatter.ofPattern("dd MMMM, yyyy"));
	        }
	    }
	 

    private GetManuallyTicketResponseDTO convertToDTO(ManuallyTickets ticket) {
        GetManuallyTicketResponseDTO dto = new GetManuallyTicketResponseDTO();
        dto.setAtmId(ticket.getAtmId());
        dto.setComment(ticket.getComment());
        dto.setCreatedBy(ticket.getCreatedBy());
        dto.setCreatedDate(ticket.getCreatedDate());
        dto.setDiagnosis(ticket.getDiagnosis());
        dto.setLastModifiedBy(ticket.getLastModifiedBy());
        dto.setLastModifiedDate(ticket.getLastModifiedDate());
        dto.setReason(ticket.getReason());
        dto.setRefNo(ticket.getRefNo());
        dto.setStatus(ticket.getStatus());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setUsername(ticket.getUsername());
        dto.setCheckerName(ticket.getCheckerName());
        dto.setCheckerTime(ticket.getCheckerTime());
        dto.setCheckerRejectReason(ticket.getCheckerRejectReason());
        dto.setCheckerComment(ticket.getCheckerComment());
        dto.setCrmStatus(ticket.getCrmStatus());
        dto.setCrmTime(ticket.getCrmTime());
        return dto;
    }
}





