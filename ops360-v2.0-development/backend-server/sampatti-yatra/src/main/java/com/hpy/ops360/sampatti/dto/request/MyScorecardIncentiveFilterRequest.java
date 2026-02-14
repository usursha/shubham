package com.hpy.ops360.sampatti.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyScorecardIncentiveFilterRequest {

	    private Integer yearId;
	    private Integer minAchieved;
	    private Integer maxAchieved;
	    private Integer sortId;
}
