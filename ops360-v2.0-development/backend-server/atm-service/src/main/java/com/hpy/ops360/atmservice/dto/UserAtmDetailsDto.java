package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAtmDetailsDto {

	private String user_login_id;

	private String user_display_name;

	private String atm_code;
}
