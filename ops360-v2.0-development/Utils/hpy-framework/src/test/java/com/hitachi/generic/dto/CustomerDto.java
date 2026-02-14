package com.hitachi.generic.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.Data;

@Data
public class CustomerDto extends GenericDto {

	private static final long serialVersionUID = 8136302120605821153L;
	private String name;
	private Double amount;
	private int age;


}
