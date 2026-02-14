package com.hpy.ops360.atmservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.entity.TicketRaisedCategoriesByAtm;
import com.hpy.ops360.atmservice.repository.TicketRaisedCategoriesByAtmRepository;
import com.hpy.ops360.atmservice.request.TicketRaisedCategoriesByAtmRequestDto;
import com.hpy.ops360.atmservice.response.TicketRaisedCategoriesByAtmResponseDto;
import com.hpy.ops360.atmservice.response.TicketRaisedResponseByAtm;

@Service
public class TicketRaisedCategoriesByAtmService {

	@Autowired
	private TicketRaisedCategoriesByAtmRepository ticketRaisedCategoriesByAtmRepository;

	@Autowired
	private LoginService loginService;

	public TicketRaisedResponseByAtm getTicketsCategories(TicketRaisedCategoriesByAtmRequestDto atmRequestDto) {
		String userId = loginService.getLoggedInUser();

		TicketRaisedCategoriesByAtm result = ticketRaisedCategoriesByAtmRepository.getTicketsByUserAndAtm(userId,
				atmRequestDto.getAtmId());

		// Process the result from the native query
		List<TicketRaisedCategoriesByAtmResponseDto> categoryList = new ArrayList<>();
		if (result != null) {

			TicketRaisedCategoriesByAtmResponseDto downCategory = new TicketRaisedCategoriesByAtmResponseDto("Down",
					result.getTotalOpenTickets(), result.getTodaysTicketsColor());
			categoryList.add(downCategory);

			// Add static data for other categories as requested
			categoryList.add(new TicketRaisedCategoriesByAtmResponseDto("SiteVisit", "0", "#00BCD4"));
			categoryList.add(new TicketRaisedCategoriesByAtmResponseDto("Adhoc", "0", "#8BC34A"));
			categoryList.add(new TicketRaisedCategoriesByAtmResponseDto("QC", "0", "#FFCC02"));
			categoryList.add(new TicketRaisedCategoriesByAtmResponseDto("Incident", "0", "#FF9800"));
			categoryList.add(new TicketRaisedCategoriesByAtmResponseDto("Recon", "0", "#9C27B0"));
			categoryList.add(new TicketRaisedCategoriesByAtmResponseDto("ESS", "0", "#2196F3"));
			categoryList.add(new TicketRaisedCategoriesByAtmResponseDto("IOT", "0", "#737373"));
		}

		return new TicketRaisedResponseByAtm(categoryList);

	}
}
