package com.hpy.ops360.ticketing.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserMobileDto extends GenericDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("mobile")
	private String mobile;

}
