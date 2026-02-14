package com.hpy.ops360.dashboard.service;

import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.entity.DownUpdatedTime;
import com.hpy.ops360.dashboard.entity.DownUpdatedTimeResponse;
import com.hpy.ops360.dashboard.repository.DownUpdatedTimeRepository;
import com.hpy.ops360.dashboard.repository.DownUpdatedTimeRequestRepository;
import com.hpy.ops360.dashboard.util.Helper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DownUpdatedTimeService {

	private DownUpdatedTimeRepository downUpdatedTimeRepository;
	private DownUpdatedTimeRequestRepository downUpdatedTimeRequestRepository;

	private Helper helper;

	public DownUpdatedTimeResponse getDownUpdatedTime(String atmId, String ticketId) {

		downUpdatedTimeRepository.deleteAll();
		downUpdatedTimeRequestRepository.deleteAll();
		DownUpdatedTime downUpdatedTime = new DownUpdatedTime();
		downUpdatedTime.setAtmId(atmId);
		downUpdatedTime.setTicketId(ticketId);
		downUpdatedTime.setUserId(helper.getLoggedInUser());
		downUpdatedTimeRequestRepository.save(downUpdatedTime);

		return downUpdatedTimeRepository.getDownUpdatedTime(downUpdatedTime.getUserId());

	}

}
