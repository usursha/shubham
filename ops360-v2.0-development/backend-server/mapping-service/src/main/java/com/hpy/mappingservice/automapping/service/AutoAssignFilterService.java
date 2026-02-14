package com.hpy.mappingservice.automapping.service;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.mappingservice.automapping.repo.AutoAssignFilterRepository;
import com.hpy.mappingservice.automapping.responseDto.AutoAssignFilterResponseDto;
import com.hpy.mappingservice.automapping.responseDto.BankInfo;
import com.hpy.mappingservice.automapping.responseDto.CityInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AutoAssignFilterService {

	@Autowired
	private AutoAssignFilterRepository repository;

//	public List<AutoAssignFilterResponseDto> getAutoAssignFilterData(String originalCEUserId) {
//		List<Object[]> results = repository.getAutoAssignFilterData(originalCEUserId);
//		List<AutoAssignFilterResponseDto> dtoList = new ArrayList<>();
//
//		for (Object[] result : results) {
//			AutoAssignFilterResponseDto dto = new AutoAssignFilterResponseDto(null, (String) result[0], // city_name
//					((Number) result[1]).longValue(), // city_count
//					(String) result[2], // bank_name
//					((Number) result[3]).longValue() // bank_count
//			);
//			dtoList.add(dto);
//		}
//
//		return dtoList;
//	}

	public AutoAssignFilterResponseDto getAutoAssignFilterData(String originalCEUserId) {
		log.info("Fetching auto assign filter data for originalCEUserId: {}", originalCEUserId);

		List<Object[]> results = repository.getAutoAssignFilterData(originalCEUserId);

		if (results.isEmpty()) {
			log.warn("No data found for originalCEUserId: {}", originalCEUserId);
			return new AutoAssignFilterResponseDto(null, new ArrayList<>(), new ArrayList<>());
		}

		// Group by bank and calculate bank counts
		Map<String, Long> bankCounts = results.stream().collect(Collectors.groupingBy(result -> (String) result[2], // bank_name
				Collectors.summingLong(result -> ((Number) result[3]).longValue()) // bank_count
		));

		// Group by city and calculate city counts
		Map<String, Long> cityCounts = results.stream().collect(Collectors.groupingBy(result -> (String) result[0], // city_name
				Collectors.summingLong(result -> ((Number) result[1]).longValue()) // city_count
		));

		// Convert to BankInfo list
		List<BankInfo> bankList = bankCounts.entrySet().stream()
				.map(entry -> new BankInfo(entry.getKey(), entry.getValue()))
				.sorted((b1, b2) -> Long.compare(b2.getBankCount(), b1.getBankCount())) // Sort by count descending
				.collect(Collectors.toList());

		// Convert to CityInfo list
		List<CityInfo> cityList = cityCounts.entrySet().stream()
				.map(entry -> new CityInfo(entry.getKey(), entry.getValue()))
				.sorted((c1, c2) -> Long.compare(c2.getCityCount(), c1.getCityCount())) // Sort by count descending
				.collect(Collectors.toList());

		log.info("Successfully processed data - Banks: {}, Cities: {}", bankList.size(), cityList.size());

		return new AutoAssignFilterResponseDto(null, bankList, cityList);
	}

	// Alternative method if you want to keep the original logic but transform at
	// the end
	public AutoAssignFilterResponseDto getAutoAssignFilterDataAlternative(String originalCEUserId) {
		log.info("Fetching auto assign filter data (alternative method) for originalCEUserId: {}", originalCEUserId);

		List<Object[]> results = repository.getAutoAssignFilterData(originalCEUserId);
		List<BankInfo> bankList = new ArrayList<>();
		List<CityInfo> cityList = new ArrayList<>();

		for (Object[] result : results) {
			String cityName = (String) result[0];
			Long cityCount = ((Number) result[1]).longValue();
			String bankName = (String) result[2];
			Long bankCount = ((Number) result[3]).longValue();

			// Add to bank list (check for duplicates)
			boolean bankExists = bankList.stream().anyMatch(bank -> bank.getBankName().equals(bankName));

			if (!bankExists) {
				bankList.add(new BankInfo(bankName, bankCount));
			} else {
				// Update existing bank count
				bankList.stream().filter(bank -> bank.getBankName().equals(bankName)).findFirst()
						.ifPresent(bank -> bank.setBankCount(bank.getBankCount() + bankCount));
			}

			// Add to city list (check for duplicates)
			boolean cityExists = cityList.stream().anyMatch(city -> city.getCityName().equals(cityName));

			if (!cityExists) {
				cityList.add(new CityInfo(cityName, cityCount));
			} else {
				// Update existing city count
				cityList.stream().filter(city -> city.getCityName().equals(cityName)).findFirst()
						.ifPresent(city -> city.setCityCount(city.getCityCount() + cityCount));
			}
		}

		// Sort lists by count in descending order
		bankList.sort((b1, b2) -> Long.compare(b2.getBankCount(), b1.getBankCount()));
		cityList.sort((c1, c2) -> Long.compare(c2.getCityCount(), c1.getCityCount()));

		log.info("Successfully processed data (alternative) - Banks: {}, Cities: {}", bankList.size(), cityList.size());

		return new AutoAssignFilterResponseDto(null, bankList, cityList);
	}
}
