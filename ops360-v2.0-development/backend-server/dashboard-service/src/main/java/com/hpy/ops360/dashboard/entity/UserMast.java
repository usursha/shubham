package com.hpy.ops360.dashboard.entity;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "view_user_mast")
public class UserMast {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "mobileno")
    private String mobileNo;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "employee_code")
    private String employeeCode;

    @Column(name = "circle_area")
    private String circleArea;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "zone")
    private String zone;
}
