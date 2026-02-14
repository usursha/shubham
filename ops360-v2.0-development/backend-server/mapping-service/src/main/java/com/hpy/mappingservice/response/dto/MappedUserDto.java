package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappedUserDto extends GenericDto{
	@JsonIgnore
	private Long id;
    private Long userId;
    private String employeeCode;
    private String userName;
    private String fullName;
    private String address;
    private String city;
	private Long mappedAtm;
	private Long assignedAtms;
	private Long remainingAtms;
	private String profilepic;
}
