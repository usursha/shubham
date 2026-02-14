package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.hpy.ops360.ticketing.cm.dto.AtmShortDetailsDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateGroupedTickets {
    private String date;
    private List<AtmShortDetailsDto> data;
}