package com.hpy.ops360.atmservice.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class OpenTicketsResponse implements Serializable {
	private static final long serialVersionUID = 8497264101713775469L;
	private String requestid;
//	@JsonProperty("atmdetails")
	private List<AtmDetailsWithTickets> atmdetails;
}
