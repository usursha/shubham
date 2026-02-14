package com.hpy.ops360.framework.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PointDto implements Serializable {
	private static final long serialVersionUID = 5699283412549727708L;
	protected double latitude;
	protected double longitude;
}
