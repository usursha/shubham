package com.hpy.ops360.atmservice.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "cm_bank_mtd_uptime_results")
public class CMWiseBankListMtdUptimeEntity {
	
	@Id
	@Column(name = "sr_no")
	private Long srNo; 
	
	@Column(name = "bank_name")
	private String bankName;  
	
	@Column(name = "mtd_uptime")
	private BigDecimal mtdUptime; 
	
	@Column(name = "overall_mtd_uptime")
	private BigDecimal overallMtdUptime; 
	
	@Column(name = "date_time")
	private String dateTime;  

}