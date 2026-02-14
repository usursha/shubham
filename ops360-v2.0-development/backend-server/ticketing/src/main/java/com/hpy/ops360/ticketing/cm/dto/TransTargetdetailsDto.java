package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransTargetdetailsDto {
	private Long srno;
    private String date;
    private String userId;
    private String transactionTrend;
    private String transactionTarget;
    private Double percentage_change;
}
