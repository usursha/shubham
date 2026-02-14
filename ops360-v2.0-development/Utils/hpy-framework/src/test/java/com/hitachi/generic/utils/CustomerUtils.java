package com.hitachi.generic.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.stereotype.Component;

import com.hitachi.generic.dto.CustomerDto;

@Component
public class CustomerUtils {

	public CustomerDto buildDto() {
		CustomerDto dto = new CustomerDto();
		dto.setId(1L);
		dto.setName("Ravi");
		dto.setAge(10);
		dto.setAmount(10.2);
		return dto;
	}

	public CustomerEntity buildEntity() {
		return buildEntity(buildDto());
	}
	public CustomerEntity buildEntity(CustomerDto dto) {
		CustomerEntity entity = new CustomerEntity();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setAge(dto.getAge());
		entity.setAmount(dto.getAmount());
		return entity;
	}
	
	public void assertDto(CustomerDto dto, CustomerDto actualDto) {
		assertNotNull(actualDto);
		assertEquals(dto.getId(), actualDto.getId());
		assertEquals(dto.getName(), actualDto.getName());
		assertEquals(dto.getAge(), actualDto.getAge());
		assertEquals(dto.getAmount(), actualDto.getAmount());
	}

	public void assertEntity(CustomerEntity expectedEntity, CustomerEntity actualEntity) {
		assertNotNull(expectedEntity);
		assertEquals(actualEntity.getId(), expectedEntity.getId());
		assertEquals(actualEntity.getName(), expectedEntity.getName());
		assertEquals(actualEntity.getAge(), expectedEntity.getAge());
		assertEquals(actualEntity.getAmount(), expectedEntity.getAmount());
	}
	
	public void assertEntityDto(CustomerEntity entity, CustomerDto dto) {
		assertNotNull(dto);
		assertEquals(entity.getId(), dto.getId());
		assertEquals(entity.getName(), dto.getName());
		assertEquals(entity.getAge(), dto.getAge());
		assertEquals(entity.getAmount(), dto.getAmount());
	}
	
	public void assertDtoEntity(CustomerDto dto, CustomerEntity actualEntity) {
		assertNotNull(actualEntity);
		assertEquals(dto.getId(), actualEntity.getId());
		assertEquals(dto.getName(), actualEntity.getName());
		assertEquals(dto.getAge(), actualEntity.getAge());
		assertEquals(dto.getAmount(), actualEntity.getAmount());
	}
}
