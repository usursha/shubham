package com.hpy.ops360.atmservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.BankWiseMTDResponseDto;
import com.hpy.ops360.atmservice.entity.BankWiseMTDEntity;
import com.hpy.ops360.atmservice.repository.BankWiseMTDRepository;

@Service
public class BankWiseMTDService {
	
	@Autowired
	private BankWiseMTDRepository bankWiseMTDRepository;
	
	public BankWiseMTDResponseDto getBankWiseMTD(String cmUserId, String bankName) {
		  	BankWiseMTDEntity data = bankWiseMTDRepository.getAllBankWiseMTDForCmUser(cmUserId, bankName); 
		  	BankWiseMTDResponseDto response=new BankWiseMTDResponseDto();
		  	response.setSrNo(data.getSrNo());
		  	response.setMtdUptime(data.getMtdUptime());
		  	
		  	return response;
	        
	}
	}
	
	
        
    

	










