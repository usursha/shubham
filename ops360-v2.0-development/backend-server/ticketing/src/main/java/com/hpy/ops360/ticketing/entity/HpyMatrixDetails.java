package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class HpyMatrixDetails {
	
	@Id
	@Column(name="sr_no")
	private Long srNo;
	
	@Column(name="mobile_number")
	private String contactNumber;

}
