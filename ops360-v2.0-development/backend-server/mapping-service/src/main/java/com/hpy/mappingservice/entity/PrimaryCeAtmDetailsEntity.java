package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
public class PrimaryCeAtmDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Column(name = "atm_id")
    private String atmId;
    
    @Column(name = "bank_name")
    private String bankName;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "assigned_ce")
    private String assignedCe;
    
    @Column(name = "dist_from_base")
    private String distFromBase;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "city")
    private String city;
    
}
