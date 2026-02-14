package com.hpy.ops360.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketAtmLocationDetailsReqDto {
 
	private String userName;
	private String ticketNumber;
	private String atmId;
	

 
}