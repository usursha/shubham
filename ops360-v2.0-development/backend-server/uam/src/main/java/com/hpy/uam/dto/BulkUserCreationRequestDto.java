package com.hpy.uam.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkUserCreationRequestDto {
	
	@NotEmpty(message="name must be atleast 4 characters long") 
	private String username;
	
	@NotEmpty(message="firstName cannot be leave blank!!")
	private String firstName;
	
	@NotEmpty(message="lastName cannot be leave blank!!")
	private String lastName;
	
	@Email
	@NotEmpty(message="email cannot be leave blank!!")
	private String email;

	@NotEmpty(message="groupName cannot be leave blank!!")
	private String groupName;
	
	@JsonIgnore
	private String password;
	
	@NotEmpty(message="MobileNumber must be 10 digits")
	@Size(min = 10, message = "{mobile number must be 10 digit long}")
	@Size(max = 10, message = "{mobile number cannot be more than 10 digit}")
	private String mobile;
	

	public BulkUserCreationRequestDto(String username, String firstName, String lastName,
			String email, String groupName,
			String mobile) {
		super();
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.groupName = groupName;
		this.password = password;
		this.mobile = mobile;
	}

	public static BulkUserCreationRequestDto fromMap(Map<String, String> map) {
		 return new BulkUserCreationRequestDto(
		            map.get("username"),
		            map.get("firstName"),
		            map.get("lastName"),
		            map.get("email"),
		            map.get("groupName"),
		            map.get("mobile")
		        );
	}
	

}
