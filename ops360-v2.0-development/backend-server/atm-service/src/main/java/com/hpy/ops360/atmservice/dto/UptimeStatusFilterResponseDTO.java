package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UptimeStatusFilterResponseDTO {

	private Integer sr_no;
	private String uptimeStatus;
	private Integer atmCount;
}
