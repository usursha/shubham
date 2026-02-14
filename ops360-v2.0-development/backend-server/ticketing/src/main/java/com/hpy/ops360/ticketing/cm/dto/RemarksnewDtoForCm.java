package com.hpy.ops360.ticketing.cm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemarksnewDtoForCm {

	@JsonIgnore
	private Long id;

	private String username;
	private String comment;
	private String date;
	private String owner;
	private String subcallType;
	
   // @JsonFormat(pattern = "dd MMMM yyyy, hh:mm a")
	private String etaTime;  // Added new field for eta_time
	
	private String vendor;
	
//	private int total_records;
	
}
