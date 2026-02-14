package com.hpy.ops360.ticketing.dto;

import java.io.Serializable;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto extends GenericDto implements Serializable {
	private static final long serialVersionUID = -7190939496252658275L;

	private String document;
	private String name;
}
