package com.hpy.uam.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UserDetailsEntity {
	
	@JsonIgnore
	@Id
	private long id;

}
