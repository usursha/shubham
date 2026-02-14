package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserAtmDetails {
	

	@Id
	private Long sr_no;

	private String user_id;

	private String user_login_id;

	private String user_display_name;

	private String atm_code;

}
