package com.hpy.mappingservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ExitedCeMappingEntity {

	@Id
    private Long id;
    
    @Column(name = "ce_full_name")
    private String ceFullName;
    
    @Column(name = "ce_profile")
    private String ceProfile;
    
    @Column(name = "ce_user_id")
    private String ceUserId;      // mahesh.patil
    
    @Column(name = "ce_id")
    private String ceId;
    
    @Column(name = "employee_id")
    private String employeeId;
    
    @Column(name = "exited_date")
    private String exitedDate;
   // private LocalDate exitedDate;
    
    @Column(name = "address")
    private String address;
    
//    @Column(name = "total_atm")
//    private Integer totalAtm;
    
    @Column(name = "assigned_atm")
    private Integer assignedAtm;
    
    @Column(name = "mapped_atm")
    private Integer mappedAtm;
    
    @Column(name = "mapped_atm_percentage")
    private String mappedAtmPercentage;
    
    @Column(name = "leave_id")
    private String leaveId;
    
}
