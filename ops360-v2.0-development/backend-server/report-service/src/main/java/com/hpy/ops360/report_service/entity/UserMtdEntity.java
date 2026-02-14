package com.hpy.ops360.report_service.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserMtdEntity {
	
	@Id
	private Long srno;
	
	private LocalDate date;  

}
