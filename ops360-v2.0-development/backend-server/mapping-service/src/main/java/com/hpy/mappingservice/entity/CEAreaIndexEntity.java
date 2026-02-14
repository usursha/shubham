package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
public class CEAreaIndexEntity {

	   @Id
	   @Column(name = "sr_no")
	   private Long srNo;
	   
	   @Column(name = "area")
	    private String area;
	    
}
