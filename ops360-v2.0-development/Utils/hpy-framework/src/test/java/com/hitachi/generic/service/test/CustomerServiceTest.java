package com.hitachi.generic.service.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hitachi.generic.dto.CustomerDto;
import com.hitachi.generic.mapper.CustomerMapper;
import com.hitachi.generic.repo.CustomerRepo;
import com.hitachi.generic.service.CustomerService;
import com.hitachi.generic.utils.CustomerEntity;
import com.hitachi.generic.utils.CustomerUtils;
import com.hpy.generic.Exception.EntityNotFoundException;

@SpringBootTest
public class CustomerServiceTest {

	
	private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

	@Autowired
	private CustomerUtils customerUtils;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private CustomerRepo customerRepo;

	private CustomerEntity manualSaveCustomer() {
		CustomerEntity entity = customerUtils.buildEntity();
		CustomerEntity saved = customerRepo.save(entity);
		return saved;
	}

	@Test
	void findById() throws EntityNotFoundException {
		CustomerEntity customer = manualSaveCustomer();
		CustomerDto dto = customerService.findById(customer.getId());
		customerUtils.assertEntityDto(customer, dto);
	}

	@Test
	void list() throws InterruptedException {
		customerRepo.deleteAll();
		CustomerEntity customer = manualSaveCustomer();
//		Thread.sleep(10000);
		List<CustomerDto> list = customerService.list();
		assertNotNull(list);
		log.error("List size is {}", list.size());
		assertTrue(list.size()==1);
		CustomerDto retreivedCustomer = list.get(0);
		customerUtils.assertEntityDto(customer, retreivedCustomer);
	}

	@Test
	void save() {
		CustomerDto dto = customerUtils.buildDto();
		CustomerDto saved = customerService.save(dto);
		customerUtils.assertDto(dto, saved);
	}

	@Test
	void update() throws EntityNotFoundException {
		CustomerEntity customer = manualSaveCustomer();
		CustomerDto preUpdateDto = customerMapper.toDto(customer);
		preUpdateDto.setAge(5);
		preUpdateDto.setName("Kumar");
		CustomerDto saved = customerService.update(preUpdateDto);
		customerUtils.assertDto(preUpdateDto, saved);
	}

	@Test
	void deleteById() {
		CustomerEntity customer = manualSaveCustomer();

		customerService.delete(customer.getId());
		Optional<CustomerEntity> removedCustomer = customerRepo.findById(customer.getId());
		assertFalse(removedCustomer.isPresent());
	}

	@Test
	void deleteByDto() {
		CustomerEntity customer = manualSaveCustomer();
		CustomerDto customerDto = customerMapper.toDto(customer);

		customerService.delete(customerDto);
		Optional<CustomerEntity> removedCustomer = customerRepo.findById(customer.getId());
		assertFalse(removedCustomer.isPresent());
	}

}
