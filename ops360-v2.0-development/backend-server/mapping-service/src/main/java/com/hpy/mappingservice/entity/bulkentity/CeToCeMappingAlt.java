package com.hpy.mappingservice.entity.bulkentity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ce_to_ce_mapping")
@Getter
@Setter
public class CeToCeMappingAlt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "primary_ce_id")
    private Integer primaryCeId;

    @Column(name = "atm_id", length = 50)
    private String atmId;

    @Column(name = "mapped_ce_id")
    private Integer mappedCeId;

    @Column(name = "from_date")
    private LocalDateTime fromDate;

    @Column(name = "to_date")
    private LocalDateTime toDate;

    @Column(name = "active")
    private Integer active;

    @Column(name = "mapped_type", length = 1)
    private String mappedType;
    
    @Column(name = "leave_request_id")
    private Long leaveRequestId;
}
