package com.hpy.ops360.sampatti.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneData {
	
	@Id
    private Long id;

    private String zoneName;
}
