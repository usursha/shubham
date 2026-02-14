package com.hpy.uam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalPersonalDetailsDto {

	private String fullName;
	private String dateOfBirth;
	private Long phoneNumber;
	private String personalEmailAddress;
	private String permanentAddress;
	private String currentAddress;
	private String profileImageId;

}
