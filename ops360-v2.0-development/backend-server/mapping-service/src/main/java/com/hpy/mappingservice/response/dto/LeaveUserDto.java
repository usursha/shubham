package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveUserDto extends GenericDto{
	@JsonIgnore
	private Long id;
    private Long userId;
    private String employeeCode;
    private String userName;
    private Long leaveId;
    private String startRange;
    private String endRange;
    private String fullName;
    private String emailId;
    private String address;
    private Long mappedAtm;
    private Long totalAtms;
    private String percentageMapped;
    private String profilePic;

}
