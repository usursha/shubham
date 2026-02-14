package com.hpy.mappingservice.entity;

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
public class ManageUpcomingLeavesGraphEntity {
	
    @Id
    @Column(name = "sr_no")
    private Integer srNo;
	
    @Column(name = "primary_ce_id")
    private Integer primaryCeId;
    
    @Column(name = "primary_ce_username")
    private String primaryCeUserName;

    @Column(name = "temp_ce_username")
    private String tempCeId;

    @Column(name = "temp_ce_full_name")
    private String tempCeFullName;
    
    @Column(name = "mapped_atm_code")
    private String mappedAtmCode;

}

