//package com.hpy.ops360.ticketing.service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.hpy.ops360.ticketing.dto.AtmDetailFilterResponseDto;
//import com.hpy.ops360.ticketing.entity.AtmDetailsFilterForCe;
//import com.hpy.ops360.ticketing.login.service.LoginService;
//import com.hpy.ops360.ticketing.repository.AtmDetailFilterRepository;
//
//@Service
//public class AtmDetailFilterForCeService {
//
//	@Autowired
//	private LoginService loginService;
//	
//	@Autowired
//	private AtmDetailFilterRepository atmDetailFilterRepository;
//	
//	public List<AtmDetailFilterResponseDto> getAtmDetailsFilterByCeId() {
//		String userLoginId = loginService.getLoggedInUser();
//		List<AtmDetailsFilterForCe> atmDetails= atmDetailFilterRepository.findAtmDetailsFilterByCeId(userLoginId);
//		return atmDetails.stream().map(this::convertToDto).collect(Collectors.toList());
//    }
//	 private AtmDetailFilterResponseDto convertToDto(AtmDetailsFilterForCe atmDetail) {
//		 AtmDetailFilterResponseDto dto = new AtmDetailFilterResponseDto();  
//	        dto.setATMID(atmDetail.getATMID());
//	        return dto;
//	    }
//}
