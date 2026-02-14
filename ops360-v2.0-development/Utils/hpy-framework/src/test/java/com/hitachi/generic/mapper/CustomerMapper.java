package com.hitachi.generic.mapper;

import org.springframework.stereotype.Component;

import com.hitachi.generic.dto.CustomerDto;
import com.hitachi.generic.utils.CustomerEntity;
import com.hpy.generic.impl.GenericMapper;

@Component
public class CustomerMapper extends GenericMapper<CustomerDto, CustomerEntity> {

	public CustomerMapper() {
		super(CustomerDto.class, CustomerEntity.class);
	}

}
