package com.hpy.mappingservice.entity.bulkentity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "atm_master")
@Getter
@Setter
public class AtmMasterAlt {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "atm_code", nullable = false, length = 255)
    private String atmCode;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "bank_name", length = 255)
    private String bankName;

    @Column(name = "city", length = 255)
    private String city;

    @Column(name = "grade", length = 255)
    private String grade;

    @Column(name = "state", length = 255)
    private String state;

    @Column(name = "zone", length = 255)
    private String zone;

    @Column(name = "source", length = 255)
    private String source;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}
