package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserMasterDto extends GenericDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private Long id;
    private Long id1;
    private String username;
    private String designation;
    private String fullName;
    private String mobileno;
    private String emailId;
    private String employeeCode;
    private String city;
    private String state;
    private String zone;
    
	public UserMasterDto(Long id1, String username, String designation, String fullName, String mobileno,
			String emailId, String employeeCode, String city, String state, String zone) {
		super();
		this.id1 = id1;
		this.username = username;
		this.designation = designation;
		this.fullName = fullName;
		this.mobileno = mobileno;
		this.emailId = emailId;
		this.employeeCode = employeeCode;
		this.city = city;
		this.state = state;
		this.zone = zone;
	}
    
    
}
