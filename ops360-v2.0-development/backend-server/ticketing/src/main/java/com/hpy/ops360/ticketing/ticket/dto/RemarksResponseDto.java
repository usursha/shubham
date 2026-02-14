package com.hpy.ops360.ticketing.ticket.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.ticketing.dto.FollowUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemarksResponseDto extends GenericDto implements Serializable {

	private static final long serialVersionUID = 5890815351229265313L;
	@JsonProperty("requestid")
	private String requestid;

	@JsonProperty("Followup")
	private List<FollowUp> followUp;

	@JsonProperty("status")
	private String status;

}
