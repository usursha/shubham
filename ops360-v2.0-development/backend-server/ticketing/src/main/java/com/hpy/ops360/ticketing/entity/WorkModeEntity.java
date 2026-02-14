package com.hpy.ops360.ticketing.entity;

import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "work_mode")
public class WorkModeEntity extends GenericEntity {

	private String mode;

}
