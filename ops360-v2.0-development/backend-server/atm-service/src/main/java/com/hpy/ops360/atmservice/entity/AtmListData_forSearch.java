package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class AtmListData_forSearch {

	@Id
	private String atmid;
    private String bankname;
    private String grade;
    private String address;
    private String machineStatus; 
    private String uptimeStatus;  
    private Integer openTickets;  
    private String transactionTrend; 
    private String mtdPerformance; 
    private String uptimeTrend;    
    private Double mtdUptime;      
    private String nameOfChannelExecutive; 
    private String nameOfSecondaryChannelExecutive; 
    private String lastVisitedOn; 
}
