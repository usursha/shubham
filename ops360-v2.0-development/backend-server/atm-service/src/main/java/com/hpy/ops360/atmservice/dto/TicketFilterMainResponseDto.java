package com.hpy.ops360.atmservice.dto;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketFilterMainResponseDto extends GenericDto{
	@JsonIgnore
	private Long id;
	private DataDTO data;
    private String totalRecords;
    private String currentPage;
    private String pageSize;
    private String totalPages;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataDTO {
        private String totalCount;
        private List<TicketsFilterResponseDto> ticketData;
    }
}