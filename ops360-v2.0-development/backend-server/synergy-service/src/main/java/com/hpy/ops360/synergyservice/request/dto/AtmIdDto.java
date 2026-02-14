package com.hpy.ops360.synergyservice.request.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmIdDto implements Serializable {

	private static final long serialVersionUID = -4542710427634533107L;
	private String atmId;

}
