package com.hpy.mappingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ManageUpcomingLeavesBankFilterEntity {
	
	@Id
	private Long srno;
	
	private String bank_name;
	
	private int count;

}
