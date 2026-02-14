package com.hpy.ops360.ticketing.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEtaRequest {

	@JsonProperty("requestid")
	private String requestid;

	@JsonProperty("ticketid")
	private String ticketid;

	@JsonProperty("atmid")
	private String atmid;

	@JsonProperty("etadatetime")
	private String etadatetime;

	@JsonProperty("updatedby")
	private String updatedby;

	@JsonProperty("comment")
	private String comment;
}
