package com.hpy.sampatti_data_service.response;

import java.util.List;

import lombok.Data;

@Data
public class ATMDataResp {

	
	    private String userId;
	    private String requestType;
	    private String timeStamp;
	    private String responseCode;
	    private String status;
	    private String responseDescription;
	    private List<BankDataDto> data;

}
