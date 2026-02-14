package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelReportRequestDto {
	private String calltype;
	private int isbreakdown;
	private String OpenDate_uid;
	private String status;

}