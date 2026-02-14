package com.hpy.ops360.ticketing.dto;

import java.io.Serializable;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmIdDto extends UserLocationDto implements Serializable {

	private static final long serialVersionUID = -4542710427634533107L;
	private String atmId;

}
