package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAtmDetails {

	@Id
	private Long sr_no;

	private String user_id;

	private String user_login_id;

	private String user_display_name;

	private String atm_code;

	private String source;
}
