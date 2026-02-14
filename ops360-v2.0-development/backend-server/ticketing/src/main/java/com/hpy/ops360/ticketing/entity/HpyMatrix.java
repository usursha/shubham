package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class HpyMatrix {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "location")
	private String location;

	@Column(name = "level")
	private Integer level;

	@Column(name = "name")
	private String contactPerson;

	@Column(name = "category")
	private String category;

	@Column(name = "email_id")
	private String email;

	@Column(name = "mobile_number")
	private String phoneNumber;

}
