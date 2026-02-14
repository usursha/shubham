package com.hpy.mappingservice.response.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveLeaveUserDto extends GenericDto{
	
	@JsonIgnore
	private Long id;
    private Integer srNo;
    private Long userId;
    private String employeeCode;
    private String userName;
    private Long leaveId;
    private String startRange;
    private String endRange;
    private LocalDateTime rawStarttime;
    private LocalDateTime rawEndtime;
    private String fullName;
    private String emailId;
    private String city;
    private String address;
    private Integer mappedAtm;
    private Integer totalAtm;
    private Integer tempCe;
    private String profilePic;
}
