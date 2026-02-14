package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CEWiseUptimeviewRequestDto {
	
	    private String rcmUserId;  // Zonal Head
	    private String scmUserId;  // State Head
	    private String cmUserId;   // Channel Manager
	    private String ceUserId;   // Channel Executive
	    private String startDate;    // Start Date for filtering
	    private String endDate;      // End Date for filtering
	    private int page;
	    private int pageSize;

}
