package com.hpy.mappingservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="atm_master")
public class AtmMaster {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "atm_code", nullable = false)
	private String atmCode;
	
	@Column(name = "bank_name")
	private String 	bankName;
	
	@Column(name = "grade")
	private String grade;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "address")
	private String address;
	
	private String zone;
	
	private String source;

	public AtmMaster(String atmCode, String bankName, String grade, String city, String state, String address,String zone,String source) {
		super();
		this.atmCode = atmCode;
		this.bankName = bankName;
		this.grade = grade;
		this.city = city;
		this.state = state;
		this.address = address;
		this.zone=zone;
		this.source=source;
	}
	
	
}
