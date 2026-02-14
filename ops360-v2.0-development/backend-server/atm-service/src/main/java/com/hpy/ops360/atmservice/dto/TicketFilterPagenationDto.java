package com.hpy.ops360.atmservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketFilterPagenationDto {

	private List<TicketsFilterResponseDto> tickets;
	private Integer totalRecords;
	private Integer currentPage;
	private Integer pageSize;
	private Integer totalPages;
}
