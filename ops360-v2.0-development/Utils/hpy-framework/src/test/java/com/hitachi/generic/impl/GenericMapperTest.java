package com.hitachi.generic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hitachi.generic.dto.CustomerDto;
import com.hitachi.generic.mapper.CustomerMapper;
import com.hitachi.generic.utils.CustomerEntity;
import com.hitachi.generic.utils.CustomerUtils;
import com.hpy.generic.Exception.EntityNotFoundException;

@SpringBootTest
class GenericMapperTest {

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private CustomerUtils customerUtils;

	

	@Test
	void testToDtoS() {
		CustomerDto dto = customerUtils.buildDto();
		CustomerEntity expectedEntity = customerUtils.buildEntity(dto);
		CustomerDto actualDto = customerMapper.toDto(expectedEntity);
		customerUtils.assertDto(dto, actualDto);
	}

	@Test
	void testToEntityT() {
		CustomerDto dto = customerUtils.buildDto();
		CustomerEntity expectedEntity = customerUtils.buildEntity(dto);
		CustomerEntity actualEntity = customerMapper.toEntity(dto);
		customerUtils.assertEntity(expectedEntity, actualEntity);
	}

	@Test
	void testToDtoOptionalOfS() throws EntityNotFoundException {
		CustomerDto dto = customerUtils.buildDto();
		CustomerEntity expectedEntity = customerUtils.buildEntity(dto);
		Optional<CustomerEntity> customerEntityOpt = Optional.of(expectedEntity);
		CustomerDto actualDto = customerMapper.toDto(customerEntityOpt);
		customerUtils.assertDto(dto, actualDto);
	}

	@Test
	void testToDtoListOfS() {
		CustomerDto dto = customerUtils.buildDto();
		CustomerEntity expectedEntity = customerUtils.buildEntity(dto);
		List<CustomerEntity> list = new ArrayList<>();
		list.add(expectedEntity);
		List<CustomerDto> actualDto = customerMapper.toDto(list);
		customerUtils.assertDto(dto, actualDto.get(0));
	}

	@Test
	void testToEntityListOfT() {
		CustomerDto dto = customerUtils.buildDto();
		CustomerEntity expectedEntity = customerUtils.buildEntity(dto);
		List<CustomerDto> list = new ArrayList<>();
		list.add(dto);
		List<CustomerEntity> actualEntity = customerMapper.toEntity(list);
		customerUtils.assertEntity(expectedEntity, actualEntity.get(0));
	}

}
