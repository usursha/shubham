package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserAtmDetails {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "user_login_id")
	private String userLoginId;

	@Column(name = "user_display_name")
	private String userDisplayName;

	@Column(name = "atm_code")
	private String atmCode;

}
