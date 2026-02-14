package com.hpy.mappingservice.automapping.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class AutoAssignCeListRequestDto {

	@NotBlank(message = "CM User ID is required")
	@JsonProperty("cm_user_id")
	private String cmUserId;

	@JsonProperty("excluded_ce_user_id")
	private String excludedCeUserId;

}
