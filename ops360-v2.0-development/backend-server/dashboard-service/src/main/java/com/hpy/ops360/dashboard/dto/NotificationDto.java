package com.hpy.ops360.dashboard.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;

@Data
public class NotificationDto extends GenericDto implements Serializable{
	
	@JsonIgnore
	private Long id;

	private String message;
}
