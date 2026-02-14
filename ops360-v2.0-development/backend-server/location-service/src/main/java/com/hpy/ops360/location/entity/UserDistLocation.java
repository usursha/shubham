package com.hpy.ops360.location.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hpy.generic.impl.GenericDto;
import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class UserDistLocation{
	
	@Id
	@Column(name = "id")
	private Long srno;
	
	private String username;
	
	private Double latitude;
	
	private Double longitude;
	
	@CreationTimestamp
    private LocalDateTime createdOn;
	

}