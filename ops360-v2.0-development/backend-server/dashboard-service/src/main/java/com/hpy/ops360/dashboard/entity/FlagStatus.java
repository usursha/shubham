package com.hpy.ops360.dashboard.entity;

import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
public class FlagStatus extends GenericEntity {

	private int flagStatus;

}
