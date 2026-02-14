package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class IndexOfUpDawnAtmForCm {

	
	@Id
	private String atm_id;
	private String machine_status;
	private String bank_name;
	private String address;
	private String grade;
	
	
	//other field 
	
	private String OpenTickets;
	private String Category;
	
	
	@Column(name = "ownership")
    private String ownership;
    
    @Column(name = "ticket_aging")
    private String ticketAging;
    
    @Column(name = "transaction_trend")
    private String transactionTrend;
    
    @Column(name = "uptime_status")
    private String uptimeStatus;
    
    @Column(name = "uptime_trend")
    private String uptimeTrend;
    
    @Column(name = "mtd_performance")
    private String mtdPerformance;
    
    @Column(name = "mtd_uptime")
    private String mtdUptime;
    
    @Column(name = "name_of_channel_executive")
    private String nameOfChannelExecutive;
    
    @Column(name = "name_of_secondary_channel_executive")
    private String nameOfSecondaryChannelExecutive;
	
}
