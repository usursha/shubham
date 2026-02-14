package com.hpy.ops360.ticketing.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketColorDto {

	private String border;
	private String fill;

//	public TicketColorDto(String border, String fill) {
//		this.border = HEX_PREFIX + border;
//		this.fill = HEX_PREFIX + fill;
//	}

}
