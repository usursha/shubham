package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AppFlagStatus {
	
	@Id
	@Column(name="srno")
	private Long srno;
	
	@Column(name="key_column")
	private String keyColumn;
	
	@Column(name="value_column")
	private Integer valueColumn;
	

}
