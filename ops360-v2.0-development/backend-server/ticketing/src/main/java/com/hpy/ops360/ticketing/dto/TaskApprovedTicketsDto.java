package com.hpy.ops360.ticketing.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskApprovedTicketsDto implements Serializable {

//	@JsonIgnore
//	private Long id;
	private static final long serialVersionUID = -1247682743373372043L;
	private String atmId;
	private String status;
	private String userName;
	private String ticketNumber; // refNo-our generated ticket number
	private String checkerRejectReason;
	private String checkerComment;
//	private String crmStatus;
}
