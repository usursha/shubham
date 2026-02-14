package com.hpy.mappingservice.response.dto;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexOfCeResponseDto extends GenericDto{
	
	
	    @JsonIgnore
	    private Long id;
	    private Long srNo;
	    private String channelExecutiveName;
	    private String employeeCode;
	    //private String state;
	    private String circleArea;
	    private String availabilityStatus;
	    private String assignedNumberOfATMs;
	    private List<String> atmAssignedIDs;
	    // List<AllAssignedAtms> data;
	    private String downMachines;
	    private String uptimeStatus;
	    private String mtdUptime;
	    //private String mtdUptimeDateTime;
	    //private String sampattiTransactionsVsTarget;
	    private String transaction;
	    private String target;
	    private String percentageChange;
	    private String totalRecords;
	    private String city;

	    
	
}
