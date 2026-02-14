package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.OwnerDto;
import com.hpy.ops360.ticketing.entity.BroadCategory;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.BroadCategoryRepository;
import com.hpy.ops360.ticketing.repository.OwnerRepository;
import com.hpy.ops360.ticketing.ticket.dto.BroadCategoryDto;
import com.hpy.ops360.ticketing.ticket.dto.SelectedCategoryDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OwnerService {

	private final OwnerRepository ownerRepository;

	private final BroadCategoryRepository broadCategoryRepository;

	private final LoginService loginService;

	public List<OwnerDto> getOwners(String broadCategory,String subcallType) {
		return ownerRepository.getOwnerList(loginService.getLoggedInUser(), broadCategory,subcallType).stream()
				.map(e -> new OwnerDto(e.getName(),e.getEtaEnable(),e.getUpdateEnable())).toList();
	}
	
	public List<OwnerDto> getSubcallTypeListPortal(String broadCategory) {
		return ownerRepository.getSubcallTypeListPortal(loginService.getLoggedInUser(), broadCategory).stream()
				.map(e -> new OwnerDto(e.getName(),e.getEtaEnable(),e.getUpdateEnable())).toList();
	}


	public SelectedCategoryDto getBroadCategory(String subcallType,String ticketNumber,String atmId) {

		List<BroadCategory> broadCategory = broadCategoryRepository.getBroadCategory(loginService.getLoggedInUser(),
				subcallType,ticketNumber,atmId);
		String selectedCategory = broadCategory.get(0).getCurrentValue(); // selected broad category based on selected
																			// subcalltype for fresh tickets
		List<BroadCategoryDto> broadCategoryList = broadCategory.stream()
				.map(e -> new BroadCategoryDto(e.getCategory())).toList();
		return new SelectedCategoryDto(selectedCategory, broadCategoryList);
	}

}
