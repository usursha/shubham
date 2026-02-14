package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDataResponse extends GenericDto {
	
    private int count;
    private List<CategoryData> data;
}
