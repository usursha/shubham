package com.hpy.sampatti_data_service.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DateDto extends GenericDto {

	private String dateTime;
}
