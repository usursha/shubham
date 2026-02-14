package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class TemporaryCeManualCeListEntity {
	
	@Id
	private Long sr_no;
	
    @Column(name = "ce_id")
    private Long ceId;
    
    @Column(name = "ce_user_id")
    private String ceUserId;

    @Column(name = "ce_full_name")
    private String ceName;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "ce_profile")
    private String ceProfile;

    @Column(name = "city")
    private String area;

    @Column(name = "address")
    private String address;

    @Column(name = "assigned_atm")
    private Integer assignedAtm;

    @Column(name = "mapped_atm")
    private Integer mappedAtm;

    @Column(name = "remaining_atm")
    private Integer remainingAtm;

    @Column(name = "atm_distance")
    private Double atmDistance;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}