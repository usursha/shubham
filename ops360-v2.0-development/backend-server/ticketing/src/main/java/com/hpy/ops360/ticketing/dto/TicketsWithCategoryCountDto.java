package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketsWithCategoryCountDto extends GenericDto {
	
	@JsonIgnore
	private Long id;

	// private Integer alltickets;
	private Integer openCount;
	private Integer updatedCount;
	private Integer timedOutCount;
	private Integer closedCount;
	
	@Builder
	public TicketsWithCategoryCountDto(Integer openCount, Integer updatedCount, Integer timedOutCount, Integer closedCount) {
		this.openCount = openCount;
		this.updatedCount = updatedCount;
		this.timedOutCount = timedOutCount;
		this.closedCount = closedCount;
	}
	
	
}
