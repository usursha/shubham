package com.hpy.sampatti_data_service.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ATMDataDto {
	
	private String atmId;
    private String grade;
    private String bankName;
    private String location;
    private int transactionTrend;
    private int uptimeTrend;
    private int downTime;
    private String reason;

}
