package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentRequestDto {

	@JsonProperty(value = "requestid")
	private String requestId;

	@JsonProperty(value = "ticketno")
	private String ticketNo;

	@JsonProperty(value = "atmid")
	private String atmId;

	@JsonProperty(value = "userid")
	private String userId;

	@JsonProperty(value = "comments")
	private String comments;

}
