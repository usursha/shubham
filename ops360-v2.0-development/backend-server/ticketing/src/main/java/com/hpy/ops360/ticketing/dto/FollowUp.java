package com.hpy.ops360.ticketing.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowUp implements Serializable {

	private static final long serialVersionUID = -1089850456287091261L;

	@JsonProperty("date")
	@JsonFormat(pattern = "dd MMMM yyyy, hh:mm a")
	private String date;

	@JsonProperty("comments")
	private String comments;

	@JsonProperty("remarks")
	private String remarks;

	@JsonProperty("username")
	private String username;
	
	@JsonProperty("subcalltype")
	private String subcalltype;
	

}
