package com.hpy.ops360.ticketing.cm.dto;

import java.time.LocalDate;

import com.hpy.ops360.ticketing.request.NotificationType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationFilterRequest {

	@NotNull(message = "creationDateId can not be null")
	@Min(value = 1)
	@Max(value = 6)
	private int creationDateId;

	@NotNull(message = "sortOption can not be null")
	@Min(value = 1)
	@Max(value = 2)
	private long sortOption;

	private NotificationType type;

	private String searchBy;
	@NotNull(message = "pageNumber can not be null")
	private int pageNumber;

	@NotNull(message = "pageSize can not be null")
	private int pageSize;

	private LocalDate fromDate;
	private LocalDate toDate;

}
