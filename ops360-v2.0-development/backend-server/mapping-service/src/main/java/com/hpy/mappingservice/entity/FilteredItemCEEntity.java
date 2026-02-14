package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class FilteredItemCEEntity {
	@Id
    private Long srno;
	
	@Column(name="record_type")
	private String recordType;
	
	@Column(name="city_id")
    private String cityId;
	
    private String city;
    private String count;
}
