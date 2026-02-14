package com.hpy.ops360.dashboard.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.dto.TicketCategoryDTO;
import com.hpy.ops360.dashboard.dto.TicketsRaisedResponseAll;
import com.hpy.ops360.dashboard.entity.TicketsRaisedEntity;
import com.hpy.ops360.dashboard.repository.TicketRaisedRepository;
import com.hpy.ops360.dashboard.util.Helper;

@Service
public class TicketsRaisedService {

	@Autowired
	private TicketRaisedRepository ticketRaisedRepository;

	@Autowired
	private Helper helper;

	  public TicketsRaisedResponseAll getTicketCategoriesForUser() {
		String  userId = helper.getLoggedInUser();
	        // Call repository method to get data for 'down' category
	        List<TicketsRaisedEntity> result = ticketRaisedRepository.getRaisedTicketsCategories(userId);
	        
	        // Process the result from the native query
	        List<TicketCategoryDTO> categoryList = new ArrayList<>();
	        
	        // We'll only use the first result which should be for the 'down' category
	        if (result != null && !result.isEmpty()) {
	            TicketsRaisedEntity downEntity = result.get(0);
	            
	            // Add down category with dynamic data
	            TicketCategoryDTO downCategory = new TicketCategoryDTO( 
	                "Down", 
	                downEntity.getTotalOpenTickets(), 
	                downEntity.getTodaysTicketColor()
	            );
	            categoryList.add(downCategory);
	            
	            // Add static data for other categories as requested
	            categoryList.add(new TicketCategoryDTO("SiteVisit", "0", "#00BCD4"));
	            categoryList.add(new TicketCategoryDTO("Adhoc", "0", "#8BC34A"));
	            categoryList.add(new TicketCategoryDTO("QC", "0", "#FFCC02"));
	            categoryList.add(new TicketCategoryDTO("Incident", "0", "#FF9800"));
	            categoryList.add(new TicketCategoryDTO("Recon", "0", "#9C27B0"));
	            categoryList.add(new TicketCategoryDTO("ESS", "0", "#2196F3"));
	            categoryList.add(new TicketCategoryDTO("IOT", "0", "#737373"));
	        }
	        
	        return new TicketsRaisedResponseAll(categoryList);
	    }
}
