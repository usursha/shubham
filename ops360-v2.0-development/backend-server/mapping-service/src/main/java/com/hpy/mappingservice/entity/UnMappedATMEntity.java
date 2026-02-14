package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnMappedATMEntity {

    @Id
    @Column(name = "sr_no")
    private Integer srno;

    @Column(name = "atm_code")
    private String atmCode;

    @Column(name = "address")
    private String address;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "city")
    private String city;

    @Column(name = "distance_from_base")
    private String distanceFromBase;
}