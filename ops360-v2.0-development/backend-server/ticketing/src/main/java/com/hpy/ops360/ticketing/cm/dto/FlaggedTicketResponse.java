package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlaggedTicketResponse extends GenericDto{
	
	@JsonIgnore
	private Long id;
	private int totalCounts;
    private String ceUserName;
    private List<FlagStatusGroup> flagStatusGroups;
}