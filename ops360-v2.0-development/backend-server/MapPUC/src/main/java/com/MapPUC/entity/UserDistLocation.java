package com.MapPUC.entity;

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
@Table(name = "ce_user_location")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDistLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long srno;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

   

    // No setter for createdOn to prevent modification after creation
}