package com.hpy.ops360.atmservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.CewiseallBanksResponseDto;
import com.hpy.ops360.atmservice.dto.allBanksResponsedto;
import com.hpy.ops360.atmservice.entity.CewiseallBanksEntity;
import com.hpy.ops360.atmservice.repository.CewiseallBanksRepository;

@Service
public class CewiseallBanksService {
	
	@Autowired
	private CewiseallBanksRepository cewiseallbanksrepository ;
	
	public List<CewiseallBanksResponseDto> getcewiseBanksList(String CeUserId){
		List<CewiseallBanksEntity> entities = cewiseallbanksrepository.getallBanksListForCeUser(CeUserId);
		return entities.stream()
				.map(entity -> new CewiseallBanksResponseDto(entity.getBankName()))
				.collect(Collectors.toList());
	}

}

