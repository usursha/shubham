package com.hpy.ops360.sampatti.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
public class FilteredItemEntity {
	@Id
    private Long srno;
	
	private String recordType;
    private String id;
    private String name;
    private String count;
}
