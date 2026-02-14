package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.AllFormattedETADatesReqDTO;
import com.hpy.ops360.ticketing.dto.AllFormattedETADatesResponseDTO;
import com.hpy.ops360.ticketing.entity.AllFormattedETADatesEntity;
import com.hpy.ops360.ticketing.repository.AllFormattedETADatesRepository;

@Service
public class AllFormattedETADatesService {

	@Autowired
	private AllFormattedETADatesRepository allFormattedETADatesRepository;

	public List<AllFormattedETADatesResponseDTO> getAllFormattedETADates(AllFormattedETADatesReqDTO req) {

		List<AllFormattedETADatesEntity> data = allFormattedETADatesRepository.getAllFormattedETADates(req.getAtmId(),
				req.getTicketNumber());

		return data.stream()
				.map(mapp -> new AllFormattedETADatesResponseDTO(mapp.getSrno(), mapp.getFormattedEtaDateTime()))
				.toList();

	}

}
