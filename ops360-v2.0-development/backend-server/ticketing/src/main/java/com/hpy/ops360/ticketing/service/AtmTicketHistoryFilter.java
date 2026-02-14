package com.hpy.ops360.ticketing.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.BroadCategoryForAtmHistoryFilterDto;
import com.hpy.ops360.ticketing.dto.SubcallTypeForAtmTicketHistoryRequestDto;
import com.hpy.ops360.ticketing.dto.SubcallTypeForAtmTicketHistoryResponseDto;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.BroadCategoryAtmHistoryFilterRepository;
import com.hpy.ops360.ticketing.repository.SubcallTypeForAtmTicketHistoryRepository;

@Service
public class AtmTicketHistoryFilter {

	@Autowired
	private BroadCategoryAtmHistoryFilterRepository broadCategoryAtmHistoryFilterRepository;
	
	@Autowired
	private SubcallTypeForAtmTicketHistoryRepository subcallTypeForAtmTicketHistoryRepository;
	
	@Autowired
	private LoginService loginService;
	
	
    public List<BroadCategoryForAtmHistoryFilterDto> getBroadCategories() {
    	String username = loginService.getLoggedInUser();
        List<Object[]> results = broadCategoryAtmHistoryFilterRepository.getBroadCategoryByUsername(username);

        // Map the result to DTOs
        return results.stream()
                .map(row -> new BroadCategoryForAtmHistoryFilterDto((String) row[1]))
                .collect(Collectors.toList());
    }
	
    
  
    public List<SubcallTypeForAtmTicketHistoryResponseDto> getOwnerList(SubcallTypeForAtmTicketHistoryRequestDto broadCategory) {
        List<Object[]> results = subcallTypeForAtmTicketHistoryRepository.getOwnerListByBroadCategory(broadCategory.getBroadCategory());

        // Map the results to DTOs
        return results.stream()
                .map(row -> new SubcallTypeForAtmTicketHistoryResponseDto((String) row[1]))
                .collect(Collectors.toList());
    }
	
    
}
