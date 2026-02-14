package com.hpy.mappingservice.entity.bulkentity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "atm_ce_mappings")
@Getter
@Setter
public class AtmCeMappingAlt {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "atm_id", nullable = false)
    private Long atmId;

    @Column(name = "ce_id", nullable = false)
    private Long ceId;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "created_date")
    private java.time.LocalDateTime createdDate;

    @Column(name = "last_modified_by", length = 255)
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private java.time.LocalDateTime lastModifiedDate;

    @Column(name = "temporary_ce_id", length = 50)
    private String temporaryCeId;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;
}
