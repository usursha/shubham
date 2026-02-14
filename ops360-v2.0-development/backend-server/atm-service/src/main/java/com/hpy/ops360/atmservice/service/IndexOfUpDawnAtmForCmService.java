package com.hpy.ops360.atmservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.IndexOfUpDawnAtmForCmResponse;
import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.dto.SeparatedIndexOfUpDawnAtmForCmResponse;
import com.hpy.ops360.atmservice.entity.IndexOfUpDawnAtmForCm;
import com.hpy.ops360.atmservice.repository.IndexOfUpDawnAtmForCmRepository;

@Service
public class IndexOfUpDawnAtmForCmService {

	@Autowired
	private IndexOfUpDawnAtmForCmRepository indexOfUpDawnAtmForCmRepository;

	@Autowired
	private LoginService loginService;

	public List<IndexOfUpDawnAtmForCmResponse> getIndexOfUpDawnAtmForCm() {
		String urserId = loginService.getLoggedInUser();
		List<IndexOfUpDawnAtmForCm> IndexOfUpDawnAtmForCm = indexOfUpDawnAtmForCmRepository
				.getIndexOfUpDawnAtmForCm(urserId);
		return IndexOfUpDawnAtmForCm.stream().map(this::convertToDTO).toList();
	}

	private IndexOfUpDawnAtmForCmResponse convertToDTO(IndexOfUpDawnAtmForCm atmForCm) {
		IndexOfUpDawnAtmForCmResponse atmForCmResponse = new IndexOfUpDawnAtmForCmResponse();
		atmForCmResponse.setAtmid(atmForCm.getAtm_id());
		atmForCmResponse.setMachine_status(atmForCm.getMachine_status());
		atmForCmResponse.setBank_name(atmForCm.getBank_name());
		atmForCmResponse.setAtm_address(atmForCm.getAddress());
		atmForCmResponse.setGrade(atmForCm.getGrade());
		atmForCmResponse.setOpenTickets(atmForCm.getOpenTickets());
		atmForCmResponse.setErrorCategory(atmForCm.getCategory());

		atmForCmResponse.setTransactionTrend(atmForCm.getTransactionTrend());
		atmForCmResponse.setUptimeStatus(atmForCm.getUptimeStatus());
		atmForCmResponse.setUptimeTrend(atmForCm.getUptimeTrend());
		atmForCmResponse.setMTDTxnPerformance(atmForCm.getMtdPerformance());
		atmForCmResponse.setMTDUptime(atmForCm.getMtdUptime());
		atmForCmResponse.setNameOfChannelExecutive(atmForCm.getNameOfChannelExecutive());
		atmForCmResponse.setNameOfSecondaryChannelExcecutive(atmForCm.getNameOfSecondaryChannelExecutive());
		atmForCmResponse.setLastVisitedOn("");
		return atmForCmResponse;

	}

}
