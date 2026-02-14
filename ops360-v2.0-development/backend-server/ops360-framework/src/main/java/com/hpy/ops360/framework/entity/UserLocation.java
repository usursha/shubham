package com.hpy.ops360.framework.entity;


import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class UserLocation extends GenericEntity {
//	protected double latitude;
//	protected double longitude;
//	protected String username;
}

