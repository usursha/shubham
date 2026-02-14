package com.hpy.ops360.sampatti.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppLeaderBoardRequestDto {
	
	private String monthYear;
	private String userType;
	private String searchKeyword;
	private String sortOrder;
	private String zoneList;
	private String stateList;
	private String cityList;
	private int pageIndex;
	private int pageSize;
	
	
}
