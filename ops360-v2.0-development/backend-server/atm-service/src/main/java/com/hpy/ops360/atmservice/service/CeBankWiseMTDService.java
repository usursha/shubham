package com.hpy.ops360.atmservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.BankWiseMTDResponseDto;
import com.hpy.ops360.atmservice.dto.CeBankWiseMTDResponseDto;
import com.hpy.ops360.atmservice.entity.BankWiseMTDEntity;
import com.hpy.ops360.atmservice.entity.CeBankWiseMTDEntity;
import com.hpy.ops360.atmservice.repository.BankWiseMTDRepository;
import com.hpy.ops360.atmservice.repository.CeBankWiseMTDRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CeBankWiseMTDService {
	

    @Autowired
    private CeBankWiseMTDRepository ceBankWiseMTDRepository;

    public CeBankWiseMTDResponseDto getCeBankWiseMTD(String ceUserId, String bankName) {
    	
    	CeBankWiseMTDEntity data = ceBankWiseMTDRepository.getAllCeBankWiseMTDForCeUser(ceUserId, bankName); 
	  	CeBankWiseMTDResponseDto response=new CeBankWiseMTDResponseDto();
	  	response.setSrNo(data.getSrNo());
	  	response.setMtdUptime(data.getMtdUptime());
	  	
	  	return response;
    	
       
    }
}





