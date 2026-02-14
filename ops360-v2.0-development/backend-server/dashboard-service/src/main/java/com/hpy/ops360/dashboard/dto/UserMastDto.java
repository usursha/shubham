package com.hpy.ops360.dashboard.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;

@Data
public class UserMastDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
	
    private String username;
    private String mobileNo;
    private String firstName;
    private String lastName;
    private String employeeCode;
    private String circleArea;
    private String userEmail;
    private String zone;
	private Integer isScreenEnable;
	private Integer isLogEnable;
}
