package com.hpy.mappingservice.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ZoneMaster {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long zoneId;

    private String zoneName;

    @OneToMany(mappedBy = "zoneMaster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<StateMaster> stateMasters;
    
    public ZoneMaster(Long id) {
        this.zoneId = id;
    }

}
