package com.hpy.ops360.ticketing.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSearchRequest {

	private String atmId; // for atmId for ticket
	private String remark; // for leave requests or announcement
}
