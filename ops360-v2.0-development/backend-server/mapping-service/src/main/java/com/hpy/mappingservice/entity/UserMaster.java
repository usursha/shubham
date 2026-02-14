package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_master")
public class UserMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "designation", nullable = false)
    private String designation;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "mobileno")
    private String mobileno;
    
    @Column(name="email_id")
    private String emailId;
    
    @Column(name = "employee_code")
    private String employeeCode;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "zone")
    private String zone;
    
    @Column(name = "home_address")
    private String homeAddress;
    
    public UserMaster(Long id) {
        this.id = id;
    }

}
