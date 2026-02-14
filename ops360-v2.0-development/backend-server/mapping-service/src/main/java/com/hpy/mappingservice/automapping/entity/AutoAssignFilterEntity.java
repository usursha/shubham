package com.hpy.mappingservice.automapping.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AutoAssignFilterEntity {

	@Id
	private String cityName;
	private Long cityCount;
	private String bankName;
	private Long bankCount;
}
