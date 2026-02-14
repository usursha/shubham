package com.hitachi.generic.utils;

import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "customer_table")
@Data
public class CustomerEntity extends GenericEntity{

	private String name;
	private Double amount;
	private int age;
	
}
