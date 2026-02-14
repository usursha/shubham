package com.hpy.mappingservice.response.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageUpcomingLeavesGraphResDto2 {
    	private String tempCeFullName;
	    private int mappedCount;
	    private List<String> mappedAtmCode;
	}

