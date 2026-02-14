package com.hpy.ops360.ticketing.dto;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketAtmLocationDetailsResDto extends GenericDto {
 
	@JsonIgnore
	private Long id;
    private Long srNo;
	private String atmId;	
	private String bankName;	
	private String model;	
	private String warranty;	
	private String siteId;	
	private String otherAtms;	
	private String address;	
	private String lastVisited;	
}
 