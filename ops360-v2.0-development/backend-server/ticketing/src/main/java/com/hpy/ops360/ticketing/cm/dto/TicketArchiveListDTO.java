package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketArchiveListDTO extends GenericDto {
	
	
	@JsonIgnore
	private Long id;
//
//	private Long srno;
//	private String bank;
//	private String atmId;
//	private String ticketNumber;
//	private String chennaleExecutive; 
	
	private List<String> bankName;
    private List<String> atmId;
    private List<String> ticketNumber;
    private List<String> channelExecutive;
	
}
