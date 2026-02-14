package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TemporaryMappingLeaveListEntity {

	@Id
    //private Long id;
    
    @Column(name = "ce_user_id")
    private String ceUserId;
    
    @Column(name = "atm_count")
    private Long atmCount;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "custom_start_time")
    private String customStartTime;
    
    @Column(name = "custom_end_time")
    private String customEndTime;
  
}
