package com.hpy.ops360.synergyservice.response.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemarksResponseDto implements Serializable {

	private static final long serialVersionUID = 5890815351229265313L;
	@JsonProperty("requestid")
	private String requestid;

	@JsonProperty("Followup")
	private List<FollowUp> followUp;

	@JsonProperty("status")
	private String status;

}
