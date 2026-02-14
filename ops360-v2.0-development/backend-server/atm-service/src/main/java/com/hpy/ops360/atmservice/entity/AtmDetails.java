package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmDetails {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "atm_code")
	private String atmCode;

	@Column(name = "Address")
	private String address;

}
