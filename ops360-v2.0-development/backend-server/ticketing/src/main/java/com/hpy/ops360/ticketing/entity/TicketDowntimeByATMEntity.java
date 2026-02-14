package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class TicketDowntimeByATMEntity {
	
	@Id
    @Column(name = "srno")	
	private Long srNo;
	
	
	 @Column(name = "createddate")	
	private String createdDate;
	 
	 @Column(name = "Duration")	
	private String duration;
	 
	 		
	

}
