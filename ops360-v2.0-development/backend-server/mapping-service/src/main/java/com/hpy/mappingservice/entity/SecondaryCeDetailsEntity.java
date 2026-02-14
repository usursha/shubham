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
public class SecondaryCeDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sr_no;
    
    @Column(name = "ce_id")
    private String ceId;
    
    @Column(name = "ce_user_id")
    private String ceUserId;
    
    @Column(name = "ce_full_name")
    private String ceFullName;
    
    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;
    
    @Column(name = "atm_distance")
    private String atmDistance;
    
    @Column(name = "mapped_atm")
    private Integer mappedAtm;
    
    @Column(name = "assigned_atm")
    private Integer assignedAtm;
    
    @Column(name = "remaining_atm")
    private Integer remainingAtm;
    
    @Column(name = "ce_profile")
    private String ceProfile;
    
    @Column(name = "employee_id")
    private String employeeId;
    
}
