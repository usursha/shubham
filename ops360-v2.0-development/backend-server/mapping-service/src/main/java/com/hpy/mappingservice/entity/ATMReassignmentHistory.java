package com.hpy.mappingservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "atm_reassignment_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATMReassignmentHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "atm_code", nullable = false, length = 50)
    private String atmCode;
    
    @Column(name = "original_ce_user_id", nullable = false, length = 100)
    private String originalCeUserId;
    
    @Column(name = "new_ce_user_id", nullable = false, length = 100)
    private String newCeUserId;
    
    @Column(name = "employee_code", length = 50)
    private String employeeCode;
    
    @Column(name = "home_address", length = 500)
    private String homeAddress;
    
    @Column(name = "atm_count")
    private Integer atmCount;
    
    @Column(name = "distance", precision = 10, scale = 3)
    private BigDecimal distance;
    
    @Column(name = "travel_time", precision = 10, scale = 6)
    private BigDecimal travelTime;
    
    @Column(name = "reassignment_date")
    private LocalDateTime reassignmentDate = LocalDateTime.now();
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Column(name = "status", length = 20)
    private String status = "PENDING";
    
    @Column(name = "remarks", length = 500)
    private String remarks;
}