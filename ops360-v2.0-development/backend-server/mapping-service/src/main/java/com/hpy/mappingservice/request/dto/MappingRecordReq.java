package com.hpy.mappingservice.request.dto;

import lombok.Data;

@Data
public class MappingRecordReq {
	
	private String atmId;
	private String ceUser;
	private String cmUser;
	private String scmUser;
	private String rcmUser;

}
