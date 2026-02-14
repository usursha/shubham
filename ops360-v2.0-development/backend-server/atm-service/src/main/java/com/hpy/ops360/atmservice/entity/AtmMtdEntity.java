package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
public class AtmMtdEntity {
	    @Id
	    @Column(name = "sr_no")	    
		private Long srNo;
	    	    
	    @Column(name = "last_updated_at")	    
		private String lastUpdatedAt;	
	    
	    @Column(name = "target_mtd")	    
		private String targetMtd;	

}