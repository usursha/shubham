package com.hpy.ops360.sampatti.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterStateCityData {
	
	@Id
	@JsonIgnore
	private Long srno;
	
	@Column(name="id")
	private Long id;
	
	@Column(name="record_type")
	private String recordType;
	
	@Column(name="name")
	private String name;
	
	@Column(name="user_count")
	private int userCount;

}
