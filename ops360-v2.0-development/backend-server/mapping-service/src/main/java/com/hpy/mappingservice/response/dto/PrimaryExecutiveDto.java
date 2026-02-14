package com.hpy.mappingservice.response.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrimaryExecutiveDto extends GenericDto {

	@JsonIgnore
	private Long id;
	private String primaryCE;
	private List<TemporaryExecutiveDto> temporaryExecutives;
}
