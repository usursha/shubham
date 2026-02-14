package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class TicketAtmLocationDetailsEntity {

 
    @Id
    @Column(name = "sr_no")	    
	private Long srNo;
    @Column(name = "atmId")	    
	private String atmId;	
	@Column(name = "bank_name")	    
	private String bankName;
	@Column(name = "model")	    
	private String model;	
	@Column(name = "warranty")	    
	private String warranty;	
	@Column(name = "siteId")	    
	private String siteId;	
	@Column(name = "otheratms")	    
	private String otherAtms;	
	@Column(name = "address")	    
	private String address;	
	@Column(name = "lastVisited")	    
	private String lastVisited;	

 
}