package com.hpy.mappingservice.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class StateMaster {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stateId;

    private String stateName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id")
    private ZoneMaster zoneMaster;

    @OneToMany(mappedBy = "stateMaster", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CityMaster> cityMasters;
    
    public StateMaster(Long id) {
        this.stateId = id;
    }

}
