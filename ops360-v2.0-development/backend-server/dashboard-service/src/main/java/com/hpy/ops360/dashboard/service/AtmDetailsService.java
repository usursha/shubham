package com.hpy.ops360.dashboard.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.dto.AtmIndentDetailsDto;
import com.hpy.ops360.dashboard.entity.AtmIndent;
import com.hpy.ops360.dashboard.entity.AtmIndentDetails;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.repository.AtmIndentDetailsRepository;
import com.hpy.ops360.dashboard.repository.AtmIndentRepository;
import com.hpy.ops360.dashboard.util.Helper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AtmDetailsService {

	private final AtmIndentRepository atmIndentRepository;

	private final AtmIndentDetailsRepository atmIndentDetailsRepository;

	private Helper helper;

	public List<AtmIndent> getAtmIndentList() {

		return atmIndentRepository.getAtmIndentList(helper.getLoggedInUser());

	}

	@Loggable
	public List<AtmIndentDetailsDto> getAtmIndentDetails(String atmCode) {
		log.info("getAtmIndentDetails| Logged_In User:{}", helper.getLoggedInUser());
		List<AtmIndentDetails> atmIndentDetails = atmIndentDetailsRepository
				.getAtmIndentDetails(helper.getLoggedInUser(), atmCode);
		log.info("getAtmIndentDetails| atmIndentDetails:{}", atmIndentDetails);

		return atmIndentDetails.stream()
				.map(result -> new AtmIndentDetailsDto(result.getSrNo(), result.getAtmCode(), result.getAccount(),
						result.getCashFillDate(), result.getCash100(), result.getCash200(), result.getCash500(),
						result.getCash2000(), result.getIndentTotal(), result.getCra()))
				.toList();
//		return atmIndentDetails;
	}
}
