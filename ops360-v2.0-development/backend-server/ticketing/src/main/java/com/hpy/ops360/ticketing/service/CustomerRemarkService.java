package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.CustomerRemarkDto;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.CustomerRemarkRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerRemarkService {

	private final CustomerRemarkRepository customerRemarkRepository;

	private final LoginService loginService;

	public List<CustomerRemarkDto> getCutomerRemarks(String subcallType,String broadCategory) {
		return customerRemarkRepository.getCustomerRemarks(loginService.getLoggedInUser(), subcallType,broadCategory).stream()
				.map(e -> new CustomerRemarkDto(e.getRemarks())).toList();

	}

}
