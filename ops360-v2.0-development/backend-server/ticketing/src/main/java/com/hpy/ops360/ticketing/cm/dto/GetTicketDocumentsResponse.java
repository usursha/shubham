package com.hpy.ops360.ticketing.cm.dto;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTicketDocumentsResponse extends GenericDto {

	@JsonIgnore
	private Long id;
	
    private int srNo;
    private String atmId;
    private String ticketNumber;
    private Date createdDate;
    private String lastModifiedBy;
    private String owner;
    private String vendor; 
    private String subcallType;
    private String internalRemark;
    private String status;
    private String ticketType;
    private int isFlagged;
    private String hoursPassed;
    private String document;
    private String documentName;
    private String document1;
    private String document1Name;
    private String document2;
    private String document2Name;
    private String document3;
    private String document3Name;
    private String document4;
    private String document4Name;
	}