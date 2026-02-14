package com.hpy.ops360.dashboard.entity;

import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "help")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Help extends GenericEntity {

	@Column(name = "name")
	private String name;

	@Column(name = "contact_no")
	private String contactNo;
}
