package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SubmitMappingReponse {
	
	@Id
	@Column(name="sr_no")
	private Long srNo;
	
	@Column(name="message")
	private String message;
	
	@Column(name="data_saved")
	private String dataSaved;

}
