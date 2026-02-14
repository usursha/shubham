package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="scheduler")
public class SchdulerEntity {
	
	@Id
	@Column(name="id")
	private Long id;
	
	@Column(name="scheduler_expression")
	private String schedulerExpression;

}
