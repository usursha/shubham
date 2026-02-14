package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketWithDocumentsDTO extends GenericDto{
	
	@JsonIgnore
	private Long id;
    private Integer srno;
    private String ticketNumber;
    private String status;
    private String ticketType;
    private Integer isFlagged;
    private String hoursPassed;
    private Integer noOfAttachments;
    private String thumbnailImage;
	public TicketWithDocumentsDTO(Integer srno, String ticketNumber, String status, String ticketType,
			Integer isFlagged, String hoursPassed, Integer noOfAttachments, String thumbnailImage) {
		super();
		this.srno = srno;
		this.ticketNumber = ticketNumber;
		this.status = status;
		this.ticketType = ticketType;
		this.isFlagged = isFlagged;
		this.hoursPassed = hoursPassed;
		this.noOfAttachments = noOfAttachments;
		this.thumbnailImage = thumbnailImage;
	}
	
    
    
}
