package com.hpy.sampatti_data_service.response;

import lombok.Data;

@Data
public class TargetDataDto {
	
	private int totalMachinesManaged;
	private int targetTransaction;
	private int targetUptime;

}
