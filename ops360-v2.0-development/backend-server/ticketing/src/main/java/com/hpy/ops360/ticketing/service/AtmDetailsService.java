//package com.hpy.ops360.ticketing.service;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hpy.ops360.ticketing.dto.AssignedAtmDto;
//import com.hpy.ops360.ticketing.dto.AssignedAtmMtdDto;
//import com.hpy.ops360.ticketing.dto.AtmDetailsDto;
//import com.hpy.ops360.ticketing.dto.AtmHistoryNTicketsResponse;
//import com.hpy.ops360.ticketing.dto.AtmIdDto;
//import com.hpy.ops360.ticketing.dto.NTicketHistory;
//import com.hpy.ops360.ticketing.dto.TicketDto;
//import com.hpy.ops360.ticketing.dto.TicketHistoryDto;
//import com.hpy.ops360.ticketing.dto.VendorDetailsDto;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
////@RequiredArgsConstructor
//public class AtmDetailsService {
//
////	private final CommonDateFormat commonDateFormat;
//
////	private final RestTemplate restTemplate;
//
//	private final SynergyService synergyService;
//
//	public AtmDetailsService(SynergyService synergyService) {
////		this.restTemplate=restTemplate;
//		this.synergyService = synergyService;
//	}
//
////	public AtmDetailsService(RestTemplate restTemplate, @Value("${synergy.base-url}") String synergyBaseUrl,
////			@Value("${synergy.username}") String username, @Value("${synergy.password}") String password) {
////		this.restTemplate = restTemplate;
////		this.synergyBaseUrl = synergyBaseUrl;
////		this.username = username;
////		this.password = password;
//////		this.commonDateFormat = commonDateFormat;
////	}
//
//	//
//
//	public AtmDetailsDto getAtmDetails() throws IOException {
//
//		try (InputStreamReader reader = new InputStreamReader(
//				new ClassPathResource("mock/atm_details.json").getInputStream())) {
//			ObjectMapper mapper = new ObjectMapper();
//			AtmDetailsDto atmDetailsDtos = mapper.readValue(reader, AtmDetailsDto.class);
//			log.debug("readValue = " + atmDetailsDtos);
//			return atmDetailsDtos;
//		} catch (IOException e) {
//			log.error("Failed to read ATM details from JSON file", e);
//			throw e;
//		}
//	}
//
//	public List<AssignedAtmDto> getAssignedAtmDetailList() throws IOException {
//
//		try (InputStreamReader reader = new InputStreamReader(
//				new ClassPathResource("mock/assigned_atm_list.json").getInputStream())) {
//			ObjectMapper mapper = new ObjectMapper();
//			List<AssignedAtmDto> assignedAtmDtos = mapper.readValue(reader, List.class);
//			log.debug("readValue = " + assignedAtmDtos);
//			return assignedAtmDtos;
//		} catch (IOException e) {
//			log.error("Failed to read ATM details from JSON file", e);
//			throw e;
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	public List<AtmIdDto> getAtmIdList() throws IOException {
//
//		try (InputStreamReader reader = new InputStreamReader(
//				new ClassPathResource("mock/atmIdList.json").getInputStream())) {
//			ObjectMapper mapper = new ObjectMapper();
//			List<AtmIdDto> atmIds = mapper.readValue(reader, List.class);
//			log.debug("readValue = " + atmIds);
//			return atmIds;
//		} catch (IOException e) {
//			log.error("Failed to read ATM details from JSON file", e);
//			throw e;
//		}
//	}
//
//	public TicketDto buildTicketDto(String atmId, String ticketNumber, String owner, String status, String problem,
//
//			int expectedTat, String creatDate, String lastUpdateDate, int completionTime) {
//		TicketDto ticketDto = new TicketDto();
//		ticketDto.setAtmId(atmId);
//		ticketDto.setTicketNumber(ticketNumber);
//		ticketDto.setOwner(owner);
//		ticketDto.setStatus(status);
//		ticketDto.setProblem(problem);
//		ticketDto.setExpectedTat(expectedTat);
////		ticketDto.setCreatDate(commonDateFormat.getStringFormattedDate(creatDate));
////		ticketDto.setLastUpdateDate(commonDateFormat.getStringFormattedDate(lastUpdateDate));
//		ticketDto.setCompletionTime(completionTime);
////		ticketDto.setRemarks(remarks);
//		return ticketDto;
//	}
//
////	public VendorDetailsDto buildVendorDetailsDto(VendorType type, String name) {
////		VendorDetailsDto vendorDetailsDto = new VendorDetailsDto();
////		vendorDetailsDto.setType(type);
////		vendorDetailsDto.setName(name);
////		return vendorDetailsDto;
////	}
//
//	public AtmDetailsDto buildAtmDetailsDto(String atmId, String address, double mtdUptime, String cra,
//			List<VendorDetailsDto> vendorDetails, List<TicketDto> ticketHistory) {
//		AtmDetailsDto atmDetails = new AtmDetailsDto();
//		atmDetails.setAtmId(atmId);
//		atmDetails.setAddress(address);
//		atmDetails.setMtdUptime(mtdUptime);
//		atmDetails.setCra(cra);
//		atmDetails.setVendorDetails(vendorDetails);
//		atmDetails.setTicketHistory(ticketHistory);
//		return atmDetails;
//	}
//
//	public List<AssignedAtmMtdDto> getAssignedAtmMtdDetailList() throws IOException {
//		try (InputStreamReader reader = new InputStreamReader(
//				new ClassPathResource("mock/assigned_atm_mtd_details.json").getInputStream())) {
//			ObjectMapper mapper = new ObjectMapper();
//			List<AssignedAtmMtdDto> assignedAtmMtdDetails = mapper.readValue(reader, List.class);
//			log.debug("readValue = " + assignedAtmMtdDetails);
//			return assignedAtmMtdDetails;
//		} catch (IOException e) {
//			log.error("Failed to read ATM details from JSON file", e);
//			throw e;
//		}
//	}
//
//	public List<NTicketHistory> getNTicketHistoryBasedOnAtmId(TicketHistoryDto ticketHistoryReqDto) {
//		AtmHistoryNTicketsResponse atmHistory = synergyService.getntickets(ticketHistoryReqDto);
//		List<NTicketHistory> ticketHistory = new ArrayList<>();
//		for (int i = 0; atmHistory.getTicketDetails() != null && i < atmHistory.getTicketDetails().size(); i++) {
//			NTicketHistory nTicketHistory = new NTicketHistory(atmHistory.getTicketDetails().get(i).getEquipmentid(),
//					atmHistory.getTicketDetails().get(i).getSrno(),
//					atmHistory.getTicketDetails().get(i).getServicecode(),
//					atmHistory.getTicketDetails().get(i).getCalltype(),
//					atmHistory.getTicketDetails().get(i).getCalldate(),
//					atmHistory.getTicketDetails().get(i).getDowntimeinmins());
//			ticketHistory.add(nTicketHistory);
//		}
//		return ticketHistory;
//	}
//
//}
