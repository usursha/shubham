package com.hpy.ops360.ticketing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.TicketGraphDetailsDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class TicketGraphDetailsService {
	
	
	public List<TicketGraphDetailsDTO> getTicketGraphDetails() {

		List<TicketGraphDetailsDTO> graphDetails = new ArrayList<>();

		graphDetails.add(new TicketGraphDetailsDTO("DOWN", "0XFFCD3497", 50));
		graphDetails.add(new TicketGraphDetailsDTO("SITE", "0XFFFF9800", 40));
		graphDetails.add(new TicketGraphDetailsDTO("ADHOC", "0XFF2196F3", 45));
		graphDetails.add(new TicketGraphDetailsDTO("INCD", "0XFF8BC34A", 30));
		graphDetails.add(new TicketGraphDetailsDTO("RECON", "0XFFFFCC02", 35));
		graphDetails.add(new TicketGraphDetailsDTO("ESS", "0XFFE91E63", 20));
		graphDetails.add(new TicketGraphDetailsDTO("ITO", "0XFF9C27B0", 40));
		graphDetails.add(new TicketGraphDetailsDTO("QC", "0XFF00BCD4", 30));

		return graphDetails;
	}
	
	
}
