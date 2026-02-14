package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CEDetails {

	@Id
	private String userDisplayName;
	private String dateOfBirth;
	private String postalAddress;
	private String mobileNo;
	private String employeeCode;
	private String circleArea;
	private String userEmail;
	private String userPhotoPath;
}
