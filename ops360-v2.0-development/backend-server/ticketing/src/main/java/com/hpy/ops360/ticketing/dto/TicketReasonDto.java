package com.hpy.ops360.ticketing.dto;

import java.io.Serializable;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketReasonDto extends GenericDto  implements Serializable {

	private static final long serialVersionUID = 1870872796794488380L;
	private String reason;

}
