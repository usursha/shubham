package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemarksDtoForCm {

	@JsonIgnore
	private Long id;

	private String username;
	private String comment;
	private String date;
	private String owner;
	private String subcallType;
	
   // @JsonFormat(pattern = "dd MMMM yyyy, hh:mm a")
	private String etaTime;  // Added new field for eta_time
}
