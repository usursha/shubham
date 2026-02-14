package com.hpy.mappingservice.entity;

import java.time.LocalDateTime;

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
public class ManageUpcomingLeaveCeAtmDetailsEntity {
	
    @Id
    @Column(name = "sr_no")
    private Long srNo;
    
    @Column(name = "atm_code")
    private String atm_code;
    
    @Column(name = "bank_name")
    private String bankName;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "dist_from_base")
    private String dist_from_base;
    
    @Column(name = "temp_ce_username")
    private String temp_ce_id;
    
    @Column(name = "temp_ce_name")
    private String temp_ce_fullName;
	

}
