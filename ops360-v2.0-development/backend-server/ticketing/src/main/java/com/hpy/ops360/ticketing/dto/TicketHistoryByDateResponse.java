package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.hpy.ops360.ticketing.cm.dto.TicketDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketHistoryByDateResponse {

	private String requestid;
    private String atmid;
    private List<TicketDetailsDto> tickets;
}
