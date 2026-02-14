package com.hpy.sampatti_data_service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenReq {

	private String userId;
	private String password;
	private String sessionID;
	private String requestTimestamp;
	private String requestType;
}
