package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AtmDataDetailsDto {

	private String atmId;
    private String bankName;
    private String grade;
    private String address;
    private String machineStatus;
    private String uptimeStatus;
    private Integer openTickets;
    private String transactionTrend;
    private String mtdPerformance;
    private String uptimeTrend;
    private Double mtdUptime;
    private String channelExecutive;
    private String secondaryChannelExecutive;
    private String lastVisitedOn;
    private Integer totalRecords;
    private Integer totalPages;
    
}
