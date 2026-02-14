package com.hpy.ops360.location.entity;

import com.hpy.generic.impl.GenericEntity;
import com.hpy.ops360.location.Point;

import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "user_location")
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class UserLocation extends GenericEntity {
	protected String username;
	protected Point point;
}
