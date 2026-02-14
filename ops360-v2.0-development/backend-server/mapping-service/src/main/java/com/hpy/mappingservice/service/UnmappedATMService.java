package com.hpy.mappingservice.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.entity.UnMappedATMEntity;
import com.hpy.mappingservice.repository.UnmappedATMRepository;
import com.hpy.mappingservice.request.CE_userId;
import com.hpy.mappingservice.request.dto.CE_unmapped_atm_dist;
import com.hpy.mappingservice.response.dto.BankCountDTO;
import com.hpy.mappingservice.response.dto.CityCountDTO;
import com.hpy.mappingservice.response.dto.DistinctATMMetadataDTO;
import com.hpy.mappingservice.response.dto.UnmappedATMResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UnmappedATMService {

	@Autowired
	private UnmappedATMRepository atmRepository;

	public List<UnmappedATMResponseDTO> getATMsForCE(CE_userId ceUserRequest) {
		String ceUsername = ceUserRequest.getExcludeCEUsername();
		log.info("Fetching unmapped ATMs for CE username: {}", ceUsername);

		List<UnMappedATMEntity> entities = atmRepository.getATMsByCEUsername(ceUsername);
		log.debug("Retrieved {} ATM records from repository for CE: {}", entities.size(), ceUsername);

		List<UnmappedATMResponseDTO> response = entities.stream().map(entity -> {
			UnmappedATMResponseDTO dto = new UnmappedATMResponseDTO();
			dto.setSrNo(entity.getSrno());
			dto.setAtmCode(entity.getAtmCode());
			dto.setAddress(entity.getAddress());
			dto.setBankName(entity.getBankName());
			dto.setCity(entity.getCity());
			dto.setDistanceFromBase(Objects.toString(entity.getDistanceFromBase(), ""));
			return dto;
		}).collect(Collectors.toList());

		log.info("Mapped {} ATM DTOs for CE: {}", response.size(), ceUsername);
		return response;
	}

	public List<UnmappedATMResponseDTO> getATMsForSecondaryCE(CE_userId ceUserRequest) {
		String ceUsername = ceUserRequest.getExcludeCEUsername();
		log.info("Fetching unmapped ATMs for CE username: {}", ceUsername);

		List<UnMappedATMEntity> entities = atmRepository.getATMsBySecondaryCEUsername(ceUsername);
		log.debug("Retrieved {} ATM records from repository for CE: {}", entities.size(), ceUsername);

		List<UnmappedATMResponseDTO> response = entities.stream().map(entity -> {
			UnmappedATMResponseDTO dto = new UnmappedATMResponseDTO();
			dto.setSrNo(entity.getSrno());
			dto.setAtmCode(entity.getAtmCode());
			dto.setAddress(entity.getAddress());
			dto.setBankName(entity.getBankName());
			dto.setCity(entity.getCity());
			dto.setDistanceFromBase(Objects.toString(entity.getDistanceFromBase(), ""));
			return dto;
		}).collect(Collectors.toList());

		log.info("Mapped {} ATM DTOs for CE: {}", response.size(), ceUsername);
		return response;
	}

	public DistinctATMMetadataDTO getFilterForSecondaryCE(CE_userId ceUserRequest) {
		String ceUsername = ceUserRequest.getExcludeCEUsername();
		log.info("Fetching unmapped ATMs for CE username: {}", ceUsername);

		List<UnMappedATMEntity> entities = atmRepository.getATMsBySecondaryCEUsername(ceUsername);
		log.debug("Retrieved {} ATM records from repository for CE: {}", entities.size(), ceUsername);

		List<UnmappedATMResponseDTO> response = entities.stream().map(entity -> {
			UnmappedATMResponseDTO dto = new UnmappedATMResponseDTO();
			dto.setSrNo(entity.getSrno());
			dto.setAtmCode(entity.getAtmCode());
			dto.setAddress(entity.getAddress());
			dto.setBankName(entity.getBankName());
			dto.setCity(entity.getCity());
			dto.setDistanceFromBase(Objects.toString(entity.getDistanceFromBase(), ""));
			return dto;
		}).collect(Collectors.toList());

		Map<String, Long> bankCountMap = response.stream().map(UnmappedATMResponseDTO::getBankName)
				.filter(name -> name != null && !name.trim().isEmpty())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		List<BankCountDTO> bankCounts = bankCountMap.entrySet().stream()
				.map(entry -> new BankCountDTO(entry.getKey(), entry.getValue().intValue()))
				.collect(Collectors.toList());

		Map<String, Long> cityCountMap = response.stream().map(UnmappedATMResponseDTO::getCity)
				.filter(city -> city != null && !city.trim().isEmpty())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		List<CityCountDTO> cityCounts = cityCountMap.entrySet().stream()
				.map(entry -> new CityCountDTO(entry.getKey(), entry.getValue().intValue()))
				.collect(Collectors.toList());

		DistinctATMMetadataDTO metadata = new DistinctATMMetadataDTO(bankCounts, cityCounts);

		log.info("Mapped {} ATM DTOs for CE: {}", response.size(), ceUsername);
		return metadata;
	}

	public DistinctATMMetadataDTO getFilterForCE(CE_userId ceUserRequest) {
		String ceUsername = ceUserRequest.getExcludeCEUsername();
		log.info("Fetching unmapped ATMs for CE username: {}", ceUsername);

		List<UnMappedATMEntity> entities = atmRepository.getATMsByCEUsername(ceUsername);
		log.debug("Retrieved {} ATM records from repository for CE: {}", entities.size(), ceUsername);

		List<UnmappedATMResponseDTO> response = entities.stream().map(entity -> {
			UnmappedATMResponseDTO dto = new UnmappedATMResponseDTO();
			dto.setSrNo(entity.getSrno());
			dto.setAtmCode(entity.getAtmCode());
			dto.setAddress(entity.getAddress());
			dto.setBankName(entity.getBankName());
			dto.setCity(entity.getCity());
			dto.setDistanceFromBase(Objects.toString(entity.getDistanceFromBase(), ""));
			return dto;
		}).collect(Collectors.toList());

		Map<String, Long> bankCountMap = response.stream().map(UnmappedATMResponseDTO::getBankName)
				.filter(name -> name != null && !name.trim().isEmpty())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		List<BankCountDTO> bankCounts = bankCountMap.entrySet().stream()
				.map(entry -> new BankCountDTO(entry.getKey(), entry.getValue().intValue()))
				.collect(Collectors.toList());

		Map<String, Long> cityCountMap = response.stream().map(UnmappedATMResponseDTO::getCity)
				.filter(city -> city != null && !city.trim().isEmpty())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		List<CityCountDTO> cityCounts = cityCountMap.entrySet().stream()
				.map(entry -> new CityCountDTO(entry.getKey(), entry.getValue().intValue()))
				.collect(Collectors.toList());

		DistinctATMMetadataDTO metadata = new DistinctATMMetadataDTO(bankCounts, cityCounts);

		log.info("Mapped {} ATM DTOs for CE: {}", response.size(), ceUsername);
		return metadata;
	}

	public List<UnmappedATMResponseDTO> getunmappedATMsdistForCE(CE_unmapped_atm_dist ce_unmapped_atm_dist) {
		log.info("Fetching unmapped ATMs for CE username: {}");

		List<UnMappedATMEntity> entities = atmRepository.getATMsWithDistance(ce_unmapped_atm_dist.getCeUsername(),
				ce_unmapped_atm_dist.getNewMappedCE(), ce_unmapped_atm_dist.getAtmCodeList());
		log.debug("Retrieved {} ATM records from repository for CE: {}", entities.size());

		List<UnmappedATMResponseDTO> response = entities.stream().map(entity -> {
			UnmappedATMResponseDTO dto = new UnmappedATMResponseDTO();
			dto.setSrNo(entity.getSrno());
			dto.setAtmCode(entity.getAtmCode());
			dto.setAddress(entity.getAddress());
			dto.setBankName(entity.getBankName());
			dto.setCity(entity.getCity());
			dto.setDistanceFromBase(Objects.toString(entity.getDistanceFromBase(), ""));
			return dto;
		}).collect(Collectors.toList());

		log.info("Mapped {} ATM DTOs for CE: {}", response.size());
		return response;
	}
}
