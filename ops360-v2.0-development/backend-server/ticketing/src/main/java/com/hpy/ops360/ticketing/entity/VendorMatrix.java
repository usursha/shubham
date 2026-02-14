package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "vendor_matrix")
public class VendorMatrix {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "state")
	private String location;

	@Column(name = "level")
	private Integer level;

	@Column(name = "name")
	private String contactPerson;

	@Column(name = "category")
	private String category;

	@Column(name = "email")
	private String email;

	@Column(name = "mobile_number")
	private String phoneNumber;

}
