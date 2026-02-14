package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@Entity
public class TemporaryCeManualAtmEntity {
	@Id
	private Long id;
	
	@Column(name = "atm_id")
    private String atm_id;
    
	@Column(name = "bank_name")
    private String bank_name;

	@Column(name = "address")
    private String address;
    
	@Column(name = "assigned_ce")
    private String assigned_ce;
	
	@Column(name = "city")
	private String city;
    
	@Column(name = "dist_from_base")
    private String dist_from_base;
    
	@Column(name = "status")
    private int status; // 0 or 1
}
