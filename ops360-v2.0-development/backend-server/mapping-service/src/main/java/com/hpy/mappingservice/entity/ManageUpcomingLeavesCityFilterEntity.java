package com.hpy.mappingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ManageUpcomingLeavesCityFilterEntity {
	
	@Id
	private Long srno;
	
	private String city_name;
	
	private int count;
	

}
