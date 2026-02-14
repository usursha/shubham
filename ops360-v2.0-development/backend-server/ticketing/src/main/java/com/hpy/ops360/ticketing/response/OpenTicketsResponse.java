package com.hpy.ops360.ticketing.response;

import java.io.Serializable;
import java.util.List;

import com.hpy.ops360.ticketing.cm.dto.AtmDetailsWithTickets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenTicketsResponse implements Serializable {
	private static final long serialVersionUID = 8497264101713775469L;
	private String requestid;
//	@JsonProperty("atmdetails")
	private List<AtmDetailsWithTickets> atmdetails;

//	public List<AtmDetailsWithTickets> getAtmdetails() {
//		return atmdetails != null ? atmdetails : new ArrayList<>();
//	}
//
//	public void setAtmdetails(List<AtmDetailsWithTickets> atmDetails) {
//		this.atmdetails = atmDetails;
//	}
}
