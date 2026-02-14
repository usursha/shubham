package com.hpy.ops360.dashboard.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenTicketsResponse extends GenericDto implements Serializable {
	
	@JsonIgnore
	private Long id;
	private static final long serialVersionUID = 8497264101713775469L;
	private String requestid;
	private List<AtmDetailsWithTickets> atmdetails;
}
