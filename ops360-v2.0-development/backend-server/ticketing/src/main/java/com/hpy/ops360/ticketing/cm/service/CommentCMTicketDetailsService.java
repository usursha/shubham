package com.hpy.ops360.ticketing.cm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.CommentCMTicketDetailsRequestDTO;
import com.hpy.ops360.ticketing.cm.repo.CommentCMTicketDetailsRepository;

@Service
public class CommentCMTicketDetailsService {

	@Autowired
	private CommentCMTicketDetailsRepository repository;

	public void insertUpdateCMTicketDetails(CommentCMTicketDetailsRequestDTO requestDTO) {
		repository.insertUpdateCMTicketDetails(requestDTO.getAtmId(), requestDTO.getTicketNumber(),
				requestDTO.getCreateBy(), requestDTO.getCmComments());
	}
	
}
