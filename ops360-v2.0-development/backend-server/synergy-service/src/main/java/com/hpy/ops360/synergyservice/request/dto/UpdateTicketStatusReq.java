package com.hpy.ops360.synergyservice.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTicketStatusReq {

	@JsonProperty("requestid")
	private String requestid;

	@JsonProperty("ticketno")
	private String ticketNo;

	@JsonProperty("atmid")
	private String atmId;

	@JsonProperty("updatedby")
	private String updatedBy;

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("documentname")
	private String documentName;

	@JsonProperty("document")
	private String document;

}
