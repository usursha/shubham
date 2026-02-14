package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
public class BroadCategory {
	
	@Id
	@Column(name="SR_NO")
	private Long srNo;
	
	@Column(name="broad_category")
	private String category;
	
	@Column(name="selected_value")
	private String currentValue;
}
