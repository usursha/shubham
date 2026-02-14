package com.hpy.ops360.sampatti.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.keycloakbase.util.LoginUtil;
import com.hpy.ops360.sampatti.dto.FinancialYearGroupDto;
import com.hpy.ops360.sampatti.entity.AppScoreCardEntity;
import com.hpy.ops360.sampatti.repository.AppScoreCardRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AppScoreCardService {
	
	@Autowired
	private AppScoreCardRepo repo;

	@Autowired
	private LoginUtil util;

	private final String financialYearData="Financial Year ";

	public List<FinancialYearGroupDto> getCEScoreCardData() {
	    String username = util.getLoggedInUserName();

	    // Fetch AppScoreCardEntity list
	    List<AppScoreCardEntity> flatList = repo.getCEScoreCardData(username);

	    // Group by financial year title using AppScoreCardEntity
	    Map<String, List<AppScoreCardEntity>> groupedByFinancialYear = flatList.stream()
	            .collect(Collectors.groupingBy(
	                    this::getFinancialYearTitle, // Group by financial year title (this method works with AppScoreCardEntity)
	                    LinkedHashMap::new,          // Maintain insertion order
	                    Collectors.toList()          // Collect into lists
	            ));

	    // Map the grouped data into FinancialYearGroupDto objects
	    List<FinancialYearGroupDto> result = groupedByFinancialYear.entrySet().stream()
	            .map(entry -> {
	                FinancialYearGroupDto dto = new FinancialYearGroupDto();
	                
	                // Set the title for the group, use the financial year from the entry key
	                dto.setTitle("Financial Year: " + entry.getKey());

	                // Set the list of AppScoreCardEntity directly into the financialYearData
	                dto.setFinancialYearData(entry.getValue());

	                return dto;
	            })
	            .collect(Collectors.toList());

	    return result;
	}

	
//	private String getFinancialYearTitle(AppScoreCardEntity request) {
//	    String month = request.getMonthName().toUpperCase();
//
//	    int year = request.getYear();
//	    List<String> months = List.of("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
//	    		"JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");
//
//	    if (months.contains(month)) {
//	        return (year - 1) + "-" + year;
//	    } else {
//	        return year + "-" + (year + 1);
//	    }
//	}
	
	private String getFinancialYearTitle(AppScoreCardEntity request) {
	    String month = request.getMonthName().toUpperCase();
	    int year = request.getYear();

	    // Months belonging to the start of a financial year
	    List<String> startMonths = List.of("APRIL", "MAY", "JUNE",
	            "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");

	    // Months belonging to the end of a financial year (Jan to Mar)
	    List<String> endMonths = List.of("JANUARY", "FEBRUARY", "MARCH");

	    if (startMonths.contains(month)) {
	        return year + "-" + (year + 1); // FY starts in current year
	    } else if (endMonths.contains(month)) {
	        return (year - 1) + "-" + year; // FY started in previous year
	    } else {
	        throw new IllegalArgumentException("Invalid month: " + month);
	    }
	}

}

