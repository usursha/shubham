package com.hpy.monitoringservice.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataDto {
	
	private int totalUsers;
	private List<CEUsersDetailsDto> userData=new ArrayList<>();
	
}
