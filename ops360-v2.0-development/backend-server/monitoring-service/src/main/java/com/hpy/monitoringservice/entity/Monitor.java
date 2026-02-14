package com.hpy.monitoringservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Monitor {
	
	@Id
	@Column(name="srno")
	private Long srno;
	
	@Column(name="key_column")
	private String keyColumn;
	
	@Column(name="value_data")
	private String valueData;

}
