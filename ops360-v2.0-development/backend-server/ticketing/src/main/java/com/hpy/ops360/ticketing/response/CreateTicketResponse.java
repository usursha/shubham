package com.hpy.ops360.ticketing.response;

import java.io.Serializable;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTicketResponse extends GenericDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6454425753396057853L;
	private String ticketno;
	private String status;
	private String requestid;
}
