package com.hpy.ops360.location;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@Embeddable
@MappedSuperclass
public class Point implements Serializable {
	private static final long serialVersionUID = 5699283412549727708L;
	protected double latitude;
	protected double longitude;
}
