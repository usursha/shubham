package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CEDetailsDto extends GenericDto {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;

	private String userDisplayName;
	private String dateOfBirth;
	private String postalAddress;
	private String mobileNo;
	private String employeeCode;
	private String circleArea;
	private String userEmail;
	private String userPhotoPath;
	private Integer isScreenEnable;
	private Integer isLogEnable;

}
