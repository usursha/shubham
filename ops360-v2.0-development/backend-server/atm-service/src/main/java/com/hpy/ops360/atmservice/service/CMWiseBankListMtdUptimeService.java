package com.hpy.ops360.atmservice.service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.hpy.ops360.atmservice.dto.CMWiseBankListMtdUptimeReqDto;
//import com.hpy.ops360.atmservice.dto.CMWiseBankListMtdUptimeResDto;
//import com.hpy.ops360.atmservice.entity.CMWiseBankListMtdUptimeEntity;
//import com.hpy.ops360.atmservice.repository.CMWiseBankListMtdUptimeRepository;
//
//@Service
//public class CMWiseBankListMtdUptimeService {
//	
//	@Autowired
//	private CMWiseBankListMtdUptimeRepository repo ;
//	
//	public List<CMWiseBankListMtdUptimeResDto> getCMWiseData(CMWiseBankListMtdUptimeReqDto request){
//		List<CMWiseBankListMtdUptimeEntity> entities = repo.getBankListDirect(request.getCmUserId());
//		return entities.stream()
//				.map(entity -> new CMWiseBankListMtdUptimeResDto(null,entity.getSrNo(), entity.getBankName(), entity.getMtdUptime(), entity.getDateTime()))
//				.collect(Collectors.toList());
//	}
//
//}

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.config.KeycloakHelper;
import com.hpy.ops360.atmservice.dto.CMWiseBankListMtdUptimeResDto;
import com.hpy.ops360.atmservice.dto.CMWiseBankListMtdUptimeResDto2;
import com.hpy.ops360.atmservice.entity.CMWiseBankListMtdUptimeEntity;
import com.hpy.ops360.atmservice.repository.CMWiseBankListMtdUptimeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CMWiseBankListMtdUptimeService {
	
	@Autowired
	private CMWiseBankListMtdUptimeRepository repo ;
	
	
	@Autowired
	private KeycloakHelper loginUtils; 
	
//	public CMWiseBankListMtdUptimeResDto2 getCMWiseData() {
//	    List<CMWiseBankListMtdUptimeEntity> entities = repo.getBankListDirect(loginUtils.getLoggedInUserName());
//
//	    // Convert entity list to DTO list
//	    List<CMWiseBankListMtdUptimeResDto> cmWiseList = entities.stream()
//	        .map(e -> new CMWiseBankListMtdUptimeResDto(
//	            null,
//	            e.getSrNo(),
//	            e.getBankName(),
//	            e.getMtdUptime(),
//	            e.getDateTime()
//	        ))
//	        .collect(Collectors.toList());
//
//	    BigDecimal overallMtdUptime = entities.isEmpty() ? BigDecimal.ZERO : entities.get(0).getOverallMtdUptime();
//
//	    return new CMWiseBankListMtdUptimeResDto2(overallMtdUptime, cmWiseList);
//	}
	
	
	
	public CMWiseBankListMtdUptimeResDto2 getCMWiseData() {
		log.info("Enter inside service CMWiseBankListMtdUptimeService :");
		List<CMWiseBankListMtdUptimeEntity> entities = repo.getBankListDirect(loginUtils.getLoggedInUserName());

	    if (entities.isEmpty()) {
	        return new CMWiseBankListMtdUptimeResDto2(Collections.emptyList());
	    }

	    BigDecimal overallMtdUptime = entities.get(0).getOverallMtdUptime();
	    String summaryDateTime = entities.get(0).getDateTime();

	    CMWiseBankListMtdUptimeResDto summaryRow = new CMWiseBankListMtdUptimeResDto(
	        null,
	        1L,
	        "All Banks",
	        overallMtdUptime,
	        summaryDateTime
	    );

	    List<CMWiseBankListMtdUptimeResDto> bankList = entities.stream()
	        .map(e -> new CMWiseBankListMtdUptimeResDto(
	            null,
	            e.getSrNo() + 1,
	            e.getBankName(),
	            e.getMtdUptime(),
	            e.getDateTime()
	        ))
	        .collect(Collectors.toList());

	    List<CMWiseBankListMtdUptimeResDto> finalList = new ArrayList<>();
	    finalList.add(summaryRow);
	    finalList.addAll(bankList);

	    return new CMWiseBankListMtdUptimeResDto2(finalList);
	}


	}