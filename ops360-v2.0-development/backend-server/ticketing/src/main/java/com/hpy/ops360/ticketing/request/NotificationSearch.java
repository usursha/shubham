package com.hpy.ops360.ticketing.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSearch {

	private NotificationType notificationType;
	private String searchParam;

	@Min(value = 1, message = "Minimum value should be 1")
	@Max(value = 2, message = "Maximum value should be 2")
	private Long sortOption;
	@Min(value = 1, message = "Minimum value should be 1")
	@Max(value = 6, message = "Maximum value should be 6")
	private Integer creationDateId;
	private LocalDate fromDate;
	private LocalDate toDate;
	
//	
}
