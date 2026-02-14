	package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsersAtmDetailsDto extends GenericDto{
	
	@JsonIgnore
	private Long id;

	private Long sr_no;

	private String user_id;

	private String user_login_id;

	private String user_display_name;

	private String atm_code;
	public UsersAtmDetailsDto(Long sr_no, String user_id, String user_login_id, String user_display_name,
			String atm_code) {
		this.sr_no = sr_no;
		this.user_id = user_id;
		this.user_login_id = user_login_id;
		this.user_display_name = user_display_name;
		this.atm_code = atm_code;
	}
}