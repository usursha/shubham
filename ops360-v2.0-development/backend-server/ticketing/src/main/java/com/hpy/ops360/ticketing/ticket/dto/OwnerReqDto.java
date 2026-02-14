package com.hpy.ops360.ticketing.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerReqDto {

	private String subcallType; //subcalltype in app
	private String broadCategory; //owner in app

}
