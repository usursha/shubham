package com.hpy.ops360.atmservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.entity.AtmTicketEvent;
import com.hpy.ops360.atmservice.entity.UserAssignedAtmDetails;
import com.hpy.ops360.atmservice.entity.UserAtmDetails;
import com.hpy.ops360.atmservice.repository.AtmTicketEventRepository;
import com.hpy.ops360.atmservice.repository.UserAssignedAtmDetailsRepository;
import com.hpy.ops360.atmservice.repository.UserAtmDetailsRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class UserAtmDetailsService {

	private final UserAtmDetailsRepository userAtmDetailsRepository;

	private final UserAssignedAtmDetailsRepository userAssignedAtmDetailsRepository;

	private final AtmTicketEventRepository atmTicketEventRepository;

	public List<UserAtmDetails> getUserAtmIds(String user_login_id) {
		List<UserAtmDetails> userAtmDetails = userAtmDetailsRepository.getUserAtmDetails(user_login_id);
		return userAtmDetails;
	}

	public List<UserAssignedAtmDetails> getUserAssignedAtmDetails(String user_login_id) {
		List<UserAssignedAtmDetails> assignedAtmDetails = userAssignedAtmDetailsRepository
				.getUserAssignedAtmDetails(user_login_id);
		return assignedAtmDetails;
	}

	public List<AtmTicketEvent> getAtmTicketEvent(String userName, String atmTicketEventcode) {
		return atmTicketEventRepository.getAtmTicketEvent(userName, atmTicketEventcode);
	}

}
