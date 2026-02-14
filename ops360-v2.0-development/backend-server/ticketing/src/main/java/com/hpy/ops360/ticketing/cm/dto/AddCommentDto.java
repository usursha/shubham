package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentDto {

	private String ticketNo;

	private String atmId;

	// private String userId;
	
	
//	private String owner;
//	
//	private String subcallType;


	private String comments;
}
