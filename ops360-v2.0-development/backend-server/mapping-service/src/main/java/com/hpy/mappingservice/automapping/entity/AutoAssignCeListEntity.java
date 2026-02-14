package com.hpy.mappingservice.automapping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AutoAssignCeListEntity {

	@Id
    @Column(name = "ce_user_id")
    private String ceUserId;
	
	@Column(name = "full_name")
	private String fullName;
	
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "employee_code")
    private String employeeCode;
    
    @Column(name = "home_address")
    private String homeAddress;
    
    @Column(name = "atm_count")
    private Integer atmCount;
    
    @Column(name = "mapped_atm")
    private Integer mappedAtm;
    
    @Column(name = "remaining")
    private Integer remaining;
	
	
}
