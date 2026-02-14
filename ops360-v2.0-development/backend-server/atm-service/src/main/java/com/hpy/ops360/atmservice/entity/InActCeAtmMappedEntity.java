package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class InActCeAtmMappedEntity {
	
	@Id
	@Column(name="srno")
	private int srno;
	
	@Column(name="message")
	private String message;
	
	@Column(name="data_message")
	private String dataMessage;

}
