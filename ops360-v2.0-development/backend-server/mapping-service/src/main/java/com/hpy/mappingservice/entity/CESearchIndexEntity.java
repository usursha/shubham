package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class CESearchIndexEntity {

	 @Id
	   @Column(name = "sr_no")
	   private Long srNo;
	
	   @Column(name = "channel_executive_name")	
	   private String channelExecutiveName;
	    
	    @Column(name = "employee_code")
	    private String employeeCode;
	    
	    @Column(name = "circle_area")
	    private String circleArea;
	    
	    @Column(name = "availability_status")
	    private String availabilityStatus;
	    
	    @Column(name = "total_atm")
	    private String assignedNumberOfATMs;
	    
	    @Column(name = "atm_assigned_ids")
	    private String atmAssignedIDs;
	    
	    @Column(name = "down_machines")
	    private String downMachines;
	    
	    @Column(name = "uptime_status")
	    private String uptimeStatus;
	    
	    @Column(name = "mtd_uptime")
	    private String mtdUptime;
	    
	    @Column(name = "transaction")
	    private String transaction;
	    
	    @Column(name = "target")
	    private String target;
	    
	    @Column(name = "percentage_change")
	    private String percentageChange;
	    
	    @Column(name = "total_records")
	    private String totalRecords;
	    
	    @Column(name = "city")
	    private String city;
	    
	    @Column(name = "state")
	    private String area;
	    
	    
	    

}
