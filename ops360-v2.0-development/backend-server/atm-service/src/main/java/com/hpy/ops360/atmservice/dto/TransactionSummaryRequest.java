package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryRequest {
	private String userType;
    private String userId;
    private String date; // Format: "yyyy-MM-dd"

}
