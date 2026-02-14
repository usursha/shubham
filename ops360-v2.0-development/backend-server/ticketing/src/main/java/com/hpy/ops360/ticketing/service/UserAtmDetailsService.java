package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.entity.UserAtmDetails;
import com.hpy.ops360.ticketing.repository.AllAtmsRepository;
import com.hpy.ops360.ticketing.repository.UserAtmDetailsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserAtmDetailsService {

	private final UserAtmDetailsRepository userAtmDetailsRepository;
	
	private final AllAtmsRepository atmsRepository;

	// method to get atm details for any type of user like CM/CE
	public List<UserAtmDetails> getUserAtmDetails(String userId) {
		return userAtmDetailsRepository.getUserAtmDetails(userId);
	}
	
	public List<String> getCmAtmList(String cmUserId) {
		return atmsRepository.getAllCmAtms(cmUserId).stream().map(atmDetails-> atmDetails.getAtmId()).toList();
	}

}
