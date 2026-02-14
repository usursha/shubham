package com.hpy.ops360.dashboard.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.dto.ATMIndexDetailsDTO;
import com.hpy.ops360.dashboard.dto.AtmDetailsDto;
import com.hpy.ops360.dashboard.dto.CEDetailsDto;
import com.hpy.ops360.dashboard.dto.CMUnderCEAtmDetailsDTO;
import com.hpy.ops360.dashboard.dto.CMUnderCEAtmDetailsDTO2;
import com.hpy.ops360.dashboard.dto.CmSynopsisDTO;
import com.hpy.ops360.dashboard.dto.FlagStatusDto;
import com.hpy.ops360.dashboard.dto.FlagStatusResponseDto;
import com.hpy.ops360.dashboard.dto.FlagStatusforCynopsysResponseDto;
import com.hpy.ops360.dashboard.entity.ATMIndexDetails;
import com.hpy.ops360.dashboard.entity.AppFlagStatus;
import com.hpy.ops360.dashboard.entity.CEDetails;
import com.hpy.ops360.dashboard.entity.CMAtmDetails;
import com.hpy.ops360.dashboard.entity.CMUnderCEAtmDetails;
import com.hpy.ops360.dashboard.entity.CmSynopsis;
import com.hpy.ops360.dashboard.entity.TransTargetdetailsEntity;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.mapper.CmSynopsisMapper;
import com.hpy.ops360.dashboard.repository.ATMIndexDetailsRepository;
import com.hpy.ops360.dashboard.repository.AppFlagStatusRepository;
import com.hpy.ops360.dashboard.repository.CMAtmDetailsRepository;
import com.hpy.ops360.dashboard.repository.CMSynapsisRepository;
import com.hpy.ops360.dashboard.repository.CMUnderCEAtmDetailsRepository;
import com.hpy.ops360.dashboard.repository.CePersonalOfficialDetailsRepository;
import com.hpy.ops360.dashboard.repository.FlagStatusRepository;
import com.hpy.ops360.dashboard.repository.TransTargetdetailsRepo;
import com.hpy.ops360.dashboard.util.Helper;
import java.util.function.Function;


import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CmSynopsisService {

	private int maxAtmListCount;

	@Autowired
	private CmSynopsisMapper cmSynopsisMapper;

	@Autowired
	private CePersonalOfficialDetailsRepository cePersonalOfficialDetailsRepository;

	@Autowired
	private CMSynapsisRepository repo;

	@Autowired
	private Helper helper;

	@Autowired
	private CMUnderCEAtmDetailsRepository cmUnderCEAtmDetailsRepository;

	@Autowired
	private FlagStatusRepository flagStatusRepository;

	@Autowired
	private ATMIndexDetailsRepository atmIndexDetailsRepository;
	
	@Autowired
	private CMAtmDetailsRepository cmAtmDetailsRepository;
	
	@Autowired
	private AppFlagStatusRepository appFlagStatusRepository;
	
	@Autowired
	private TransTargetdetailsRepo repository;

	@Loggable
	public List<CmSynopsisDTO> getCmSynopsisDetails() {
		String userId = helper.getLoggedInUser();
		long currentTime = System.currentTimeMillis();
		List<CmSynopsis> list = repo.callGetCMSynapsisList(userId);
		
		log.warn("Time for A2-Exec: {} seconds", (System.currentTimeMillis() - currentTime) / 1000);
		return cmSynopsisMapper.toDto(list);
	}
	
	
	@Loggable
	public List<CmSynopsisDTO> getCmSynopsisDetailsv2() {
	    String userId = helper.getLoggedInUser();

	    List<CmSynopsis> list = repo.callGetCMSynapsisList(userId);
	    List<TransTargetdetailsEntity> rawResults = repository.gettranstargetdetail(
	        userId, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
	    );

	    // Map userId to TransTargetdetailsEntity
	    Map<String, TransTargetdetailsEntity> transMap = rawResults.stream()
	        .collect(Collectors.toMap(TransTargetdetailsEntity::getUserId, Function.identity()));

	    return list.stream()
	        .map(synopsis -> {
	            CmSynopsisDTO dto = new CmSynopsisDTO();

	            // Map from CmSynopsis
	            dto.setCurrentStatus(synopsis.getCurrentStatus());
	            dto.setTimeLaps(synopsis.getTimeLaps());
	            dto.setFlagStatus(synopsis.getFlagStatus());
	            dto.setChannelExecutiveName(synopsis.getChannelExecutiveName());
	            dto.setTotalUptime(synopsis.getTotalUptime());
	            dto.setDownMachines(synopsis.getDownMachines());
	            dto.setVisitCompletion(synopsis.getVisitCompletion());
	            dto.setDistanceCovered(synopsis.getDistanceCovered());
	            dto.setCountFlagStatus(synopsis.getCountFlagStatus());
	            dto.setTicketEx(synopsis.getTicketEx());
//	            dto.setTrgtColourCode(synopsis.getTrgtColourCode());
	            dto.setSampathiTrgt(synopsis.getSampathiTrgt());
	            dto.setCeUserName(synopsis.getCeUserName());

	            // Map from TransTargetdetailsEntity using CeUserName
	            TransTargetdetailsEntity trans = transMap.get(synopsis.getCeUserName());
	            

	            String transactionTrend = trans != null && trans.getTransactionTrend() != null ? trans.getTransactionTrend() : "";
	            String transactionTarget = trans != null && trans.getTransactionTarget() != null ? trans.getTransactionTarget() : "";
//	            Double percentageChange=trans != null && trans.getPercentage_change() != null ? trans.getPercentage_change() : null;
	            
	            dto.setTransactionachieved(transactionTrend);
	            dto.setTarget(transactionTarget);
	            dto.setPercentage_change(trans.getPercentage_change());
	            dto.setTrgtColourCode(getTransactionColor(transactionTarget, transactionTrend));

	            return dto;
	        })
	        .collect(Collectors.toList());
	}
	
	
	private String getTransactionColor(String target, String txnTrend) {
	    if (target != null && !target.isEmpty() && txnTrend != null && !txnTrend.isEmpty()) {
	        double txnTrendValue = Double.parseDouble(txnTrend);
	        double targetValue = Double.parseDouble(target);

	        if (targetValue == 0) return "#FF0026"; // Avoid division by zero

	        double value = (txnTrendValue / targetValue) * 100;

	        if (value < 92.00) {
	            return "#FF0026"; // Red
	        } else if (value >= 92.00 && value < 95.00) {
	            return "#FF7A00"; // Orange
	        } else {
	            return "#23A36D"; // Green
	        }
	    }
	    return "#FF0026"; // Default color if data is missing
	}




	@Loggable
	public CEDetailsDto getCEDetails(String ceUserId) {
		CEDetails CEDetailsresponse = cePersonalOfficialDetailsRepository.getCEDetails(ceUserId);
		List<AppFlagStatus> appFlagStatuses = appFlagStatusRepository.getAppFlagStatus(ceUserId);
		return getCeDetailsResponseMapper(CEDetailsresponse,appFlagStatuses);
	}

	@Loggable
	private CEDetailsDto getCeDetailsResponseMapper(CEDetails cedetails, List<AppFlagStatus> appFlagStatuses) {
		CEDetailsDto ceresponseDto = new CEDetailsDto();
		if (cedetails != null) {
		ceresponseDto.setUserDisplayName(cedetails.getUserDisplayName() != null && !cedetails.getUserDisplayName().isEmpty() ? cedetails.getUserDisplayName() : "");
		ceresponseDto.setDateOfBirth(cedetails.getDateOfBirth() != null && !cedetails.getDateOfBirth().isEmpty() ? cedetails.getDateOfBirth() : "");
		ceresponseDto.setPostalAddress(cedetails.getPostalAddress() != null && !cedetails.getPostalAddress().isEmpty() ? cedetails.getPostalAddress() : "");

		ceresponseDto.setMobileNo(cedetails.getMobileNo() != null && !cedetails.getMobileNo().isEmpty() ? cedetails.getMobileNo() : "");
		ceresponseDto.setEmployeeCode(cedetails.getEmployeeCode() != null && !cedetails.getEmployeeCode().isEmpty() ? cedetails.getEmployeeCode() : "");
		ceresponseDto.setCircleArea(cedetails.getCircleArea() != null && !cedetails.getCircleArea().isEmpty() ? cedetails.getCircleArea() : "");
		ceresponseDto.setUserEmail(cedetails.getUserEmail() != null && !cedetails.getUserEmail().isEmpty() ? cedetails.getUserEmail() : "");
		ceresponseDto.setUserPhotoPath(cedetails.getUserPhotoPath() != null && !cedetails.getUserPhotoPath().isEmpty() ? cedetails.getUserPhotoPath() : "");
		}
		else {
			ceresponseDto.setUserDisplayName("");
	        ceresponseDto.setDateOfBirth("");
	        ceresponseDto.setPostalAddress("");
	        ceresponseDto.setMobileNo("");
	        ceresponseDto.setEmployeeCode("");
	        ceresponseDto.setCircleArea("");
	        ceresponseDto.setUserEmail("");
	        ceresponseDto.setUserPhotoPath("");
		}
		ceresponseDto.setIsScreenEnable(appFlagStatuses.isEmpty()?0:appFlagStatuses.get(0).getValueColumn());
		ceresponseDto.setIsLogEnable(appFlagStatuses.isEmpty()?0:appFlagStatuses.get(1).getValueColumn());
		
		return ceresponseDto;

	}

	@Loggable
	public List<AtmDetailsDto> getCEAtmListUnderCM(String ceUserId) {
		List<CMUnderCEAtmDetails> detailsList = cmUnderCEAtmDetailsRepository.getCMUnderCEAtmDetails(ceUserId);
		return detailsList.stream().map(details -> {
			return new AtmDetailsDto(details.getAtmid());
		}).collect(Collectors.toList());

	}

	@Loggable
	public List<CMUnderCEAtmDetailsDTO> getCMUnderCEAtmDetails(String ceUserId) {
		List<CMUnderCEAtmDetails> detailsList = cmUnderCEAtmDetailsRepository.getCMUnderCEAtmDetails(ceUserId);
		return detailsList.stream().map(details -> {
			return fillCEAtmDetails(details);
		}).collect(Collectors.toList());
	}
	
	@Loggable
	public List<CMUnderCEAtmDetailsDTO2> getCMAtmDetails(String ceUserId) {
		List<CMAtmDetails> detailsList = cmAtmDetailsRepository.getCMAtmDetails(ceUserId);
		return detailsList.stream().map(details -> {
			return fillCMAtmDetails(details);
		}).toList();
	}
	
	@Loggable
	public List<CMUnderCEAtmDetailsDTO2> getCMAtmDetailsExcelAndCSV(String ceUserId) {
		List<CMAtmDetails> detailsList = cmAtmDetailsRepository.getCMAtmDetailsExcelAndCSV(ceUserId);
		return detailsList.stream().map(details -> {
			return fillCMAtmDetails(details);
		}).toList();
	}
	
	private CMUnderCEAtmDetailsDTO fillCEAtmDetails(CMUnderCEAtmDetails details) {
		CMUnderCEAtmDetailsDTO dto = new CMUnderCEAtmDetailsDTO();
		dto.setId(details.getId());
		dto.setAtmid(details.getAtmid());
		dto.setBankname(details.getBankname());
		dto.setGrade(details.getGrade());
		dto.setAddress(details.getAddress());
		dto.setMachineStatus(details.getMachineStatus());
		dto.setOpenTickets(details.getOpenTickets());
//		dto.setErrorCategory(details.getErrorCategory());
//		dto.setOwnership(details.getOwnership());
//		dto.setTicketAging(details.getTicketAging());
		dto.setTransactionTrend(details.getTransactionTrend() == null ? "" : details.getTransactionTrend());
		dto.setMtdPerformance(details.getMtdPerformance() == null ? "" : details.getMtdPerformance());
		dto.setUptimeTrend(details.getUptimeTrend());
		//dto.setUptimeStatus("");
		dto.setUptimeStatus(details.getUptimeStatus() == null ? "" : details.getUptimeStatus());
		dto.setMtdUptime(details.getMtdUptime());
		dto.setNameOfChannelExecutive(details.getNameOfChannelExecutive());
		dto.setNameOfSecondaryChannelExecutive(details.getNameOfSecondaryChannelExecutive());
		dto.setLastVisitedOn(details.getLastVisitedOn() == null ? "" : details.getLastVisitedOn());
		return dto;
	}
	
	private CMUnderCEAtmDetailsDTO2 fillCMAtmDetails(CMAtmDetails details) {
		CMUnderCEAtmDetailsDTO2 dto = new CMUnderCEAtmDetailsDTO2();
		dto.setId(details.getId());
		dto.setAtmid(details.getAtmid());
		dto.setBankname(details.getBankname());
		dto.setGrade(details.getGrade());
		dto.setAddress(details.getAddress());
		dto.setMachineStatus(details.getMachineStatus());
		dto.setOpenTickets(details.getOpenTickets());
//		dto.setErrorCategory(details.getErrorCategory());
//		dto.setOwnership(details.getOwnership());
//		dto.setTicketAging(details.getTicketAging());
		dto.setTransactionTrend(details.getTransactionTrend() == null ? "" : details.getTransactionTrend());
		dto.setMtdPerformance(details.getMtdPerformance() == null ? "" : details.getMtdPerformance());
		dto.setUptimeTrend(details.getUptimeTrend());
		//dto.setUptimeStatus("");
		dto.setUptimeStatus(details.getUptimeStatus() == null ? "" : details.getUptimeStatus());
		dto.setMtdUptime(details.getMtdUptime());
		dto.setNameOfChannelExecutive(details.getNameOfChannelExecutive());
		dto.setNameOfSecondaryChannelExecutive(details.getNameOfSecondaryChannelExecutive());
		dto.setLastVisitedOn(details.getLastVisitedOn() == null ? "" : details.getLastVisitedOn());
		return dto;
	}

	public FlagStatusResponseDto getTravelStatus(String flagStatus) {
		String userId = helper.getLoggedInUser();

		List<CmSynopsis> list = repo.callGetCMSynapsisList(userId);

		FlagStatusResponseDto response = new FlagStatusResponseDto();

		return response;
	}

	@Loggable
	public FlagStatusforCynopsysResponseDto insertOrUpdateFlagStatus(FlagStatusDto flagStatusDto) {
        try {
            String ceUserId = flagStatusDto.getCeUserId();
            String cmUserId = helper.getLoggedInUser();
            int flagStatus = flagStatusDto.getFlagStatus();
            
            // Execute stored procedure
            List<Object[]> result = flagStatusRepository.callInsertUpdateFlagstatus(ceUserId, cmUserId, flagStatus);
            
            if (result != null && !result.isEmpty()) {
                Object[] row = result.get(0);
                int retrievedFlagStatus = ((Number) row[0]).intValue();
                int flaggedTicketsCount = ((Number) row[1]).intValue();
                return new FlagStatusforCynopsysResponseDto(retrievedFlagStatus, flaggedTicketsCount);
            }
            
            // Return default response if no results
            return new FlagStatusforCynopsysResponseDto(flagStatus, 0);
            
        } catch (Exception e) {
            log.error("Error in insertOrUpdateFlagStatus: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update flag status", e);
        }
    }
	
	public ATMIndexDetailsDTO getATMIndexDetails(String atmId) {
		ATMIndexDetails details = atmIndexDetailsRepository.getATMIndexDetails(atmId);

		ATMIndexDetailsDTO dto = new ATMIndexDetailsDTO();
		dto.setAtmId(details.getAtmId());
		dto.setPsid(details.getPsid());
		dto.setSerialNo(details.getSerialNo());
		dto.setBankAccount(details.getBankAccount());
		dto.setSiteCategory(details.getSiteCategory());
		dto.setTechLiveDate(details.getTechLiveDate());
		dto.setClosureDate(details.getClosureDate());
		dto.setAddress(details.getAddress());
		dto.setCity(details.getCity());
		dto.setPincode(details.getPincode());
		dto.setDistrict(details.getDistrict());
		dto.setState(details.getState());
		dto.setSiteLatitude(details.getSiteLatitude());
		dto.setSiteLongitude(details.getSiteLongitude());
		dto.setCircle(details.getCircle());
	    dto.setAtmVpiId(details.getAtmVpiId());
	    dto.setPaid(details.getPaid());
	    dto.setSourcingStrategy(details.getSourcingStrategy());
	    dto.setSiteType(details.getSiteType());
	    dto.setSiteClassification(details.getSiteClassification());
	    dto.setProjectType(details.getProjectType());
	    dto.setAtmCrmUpi(details.getAtmCrmUpi());
	    dto.setAtmOem(details.getAtmOem());
	    dto.setCashLiveDate(details.getCashLiveDate());
	    dto.setAtmHandoverDate(details.getAtmHandoverDate());

        dto.setSiteName(details.getSiteName());
        dto.setAtmStatus(details.getAtmStatus());
        dto.setParentBranch(details.getParentBranch());
        dto.setSiteAccessHours(details.getSiteAccessHours());
        dto.setLandlordName(details.getLandlordName());
        dto.setContactNumber(details.getContactNumber());
        dto.setLandlordCity(details.getLandlordCity());
        dto.setLandlordEmailId(details.getLandlordEmailId());
        

        dto.setParentCity(details.getParentCity());
        dto.setParentPinCode(details.getParentPinCode());
        dto.setParentDistrict(details.getParentDistrict());
        dto.setParentAddress(details.getParentAddress());
        dto.setParentState(details.getParentState());
        dto.setParentCircle(details.getParentCircle());

		return dto;
	}

	
	//-----------for csv format-----------------------------------
	
	@Loggable
	public ByteArrayResource generateCsvResource(String userId) throws IOException {
		List<CMUnderCEAtmDetailsDTO> atmDetailsDTO = getCMUnderCEAtmDetails(userId);
		StringBuilder csvContent = new StringBuilder();
		csvContent.append(
				"ID,ATM ID,Bank Name,Grade,Address,Machine Status,Open Tickets,Error Category,Ownership,Ticket Aging,Transaction Trend,MTD Performance,Uptime Trend,MTD Uptime,Name of Channel Executive,Name of Secondary Channel Executive,Last Visited On\n");

		for (CMUnderCEAtmDetailsDTO atm : atmDetailsDTO) {
			csvContent.append(String.format("%d,%s,%s,%s,\"%s\",%s,%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", atm.getId(),
					atm.getAtmid(), atm.getBankname(), atm.getGrade(), atm.getAddress().replace("\"", "\"\""),
					atm.getMachineStatus(), atm.getOpenTickets(), atm.getErrorCategory(), atm.getOwnership(),
					atm.getTicketAging(), atm.getTransactionTrend(), atm.getMtdPerformance(), atm.getUptimeTrend(),
					atm.getMtdUptime(), atm.getNameOfChannelExecutive(), atm.getNameOfSecondaryChannelExecutive(),
					atm.getLastVisitedOn()));
		}

		return new ByteArrayResource(csvContent.toString().getBytes());
	}
	
	@Loggable
	public ByteArrayResource generateCsvResourceAtmIndex(String userId) throws IOException {
		List<CMUnderCEAtmDetailsDTO2> atmDetailsDTO = getCMAtmDetailsExcelAndCSV(userId);
		StringBuilder csvContent = new StringBuilder();
		csvContent.append(
				"ID,ATM ID,Bank Name,Grade,Address,Machine Status,Open Tickets,Error Category,Ownership,Ticket Aging,Transaction Trend,MTD Performance,Uptime Trend,MTD Uptime,Name of Channel Executive,Name of Secondary Channel Executive,Last Visited On\n");

		for (CMUnderCEAtmDetailsDTO2 atm : atmDetailsDTO) {
			csvContent.append(String.format("%d,%s,%s,%s,\"%s\",%s,%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", atm.getId(),
					atm.getAtmid(), atm.getBankname(), atm.getGrade(), atm.getAddress().replace("\"", "\"\""),
					atm.getMachineStatus(), atm.getOpenTickets(), atm.getErrorCategory(), atm.getOwnership(),
					atm.getTicketAging(), atm.getTransactionTrend(), atm.getMtdPerformance(), atm.getUptimeTrend(),
					atm.getMtdUptime(), atm.getNameOfChannelExecutive(), atm.getNameOfSecondaryChannelExecutive(),
					atm.getLastVisitedOn()));
		}

		
		return new ByteArrayResource(csvContent.toString().getBytes());
	}
	
	@Loggable
	public String generateCsvResourceAtmIndex_base64(String userId) throws IOException {
		List<CMUnderCEAtmDetailsDTO2> atmDetailsDTO = getCMAtmDetailsExcelAndCSV(userId);
		StringBuilder csvContent = new StringBuilder();
		csvContent.append(
				"ID,ATM ID,Bank Name,Grade,Address,Machine Status,Open Tickets,Error Category,Ownership,Ticket Aging,Transaction Trend,MTD Performance,Uptime Trend,MTD Uptime,Name of Channel Executive,Name of Secondary Channel Executive,Last Visited On\n");
		
		for (CMUnderCEAtmDetailsDTO2 atm : atmDetailsDTO) {
			csvContent.append(String.format("%d,%s,%s,%s,\"%s\",%s,%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", atm.getId(),
					atm.getAtmid(), atm.getBankname(), atm.getGrade(), atm.getAddress().replace("\"", "\"\""),
					atm.getMachineStatus(), atm.getOpenTickets(), atm.getErrorCategory(), atm.getOwnership(),
					atm.getTicketAging(), atm.getTransactionTrend(), atm.getMtdPerformance(), atm.getUptimeTrend(),
					atm.getMtdUptime(), atm.getNameOfChannelExecutive(), atm.getNameOfSecondaryChannelExecutive(),
					atm.getLastVisitedOn()));
		}
		
		// Convert CSV content to base64
	    byte[] csvBytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);
	    return Base64.getEncoder().encodeToString(csvBytes);
		
		//return new ByteArrayResource(csvContent.toString().getBytes());
	}
	
	// In the service class
	
	@Loggable
	public String generateBase64CsvContent(String userId) throws IOException {
	    List<CMUnderCEAtmDetailsDTO> atmDetailsDTO = getCMUnderCEAtmDetails(userId);
	    StringBuilder csvContent = new StringBuilder();
	    
	    // Add CSV headers
	    csvContent.append(
	            "ID,ATM ID,Bank Name,Grade,Address,Machine Status,Open Tickets,Error Category,Ownership,Ticket Aging,Transaction Trend,MTD Performance,Uptime Trend,MTD Uptime,Name of Channel Executive,Name of Secondary Channel Executive,Last Visited On\n");
	    
	    // Add data rows
	    for (CMUnderCEAtmDetailsDTO atm : atmDetailsDTO) {
	        csvContent.append(String.format("%d,%s,%s,%s,\"%s\",%s,%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", 
	            atm.getId(),
	            atm.getAtmid(), 
	            atm.getBankname(), 
	            atm.getGrade(), 
	            atm.getAddress().replace("\"", "\"\""),
	            atm.getMachineStatus(), 
	            atm.getOpenTickets(), 
	            atm.getErrorCategory(), 
	            atm.getOwnership(),
	            atm.getTicketAging(), 
	            atm.getTransactionTrend(), 
	            atm.getMtdPerformance(), 
	            atm.getUptimeTrend(),
	            atm.getMtdUptime(), 
	            atm.getNameOfChannelExecutive(), 
	            atm.getNameOfSecondaryChannelExecutive(),
	            atm.getLastVisitedOn()));
	    }
	    
	    // Convert to Base64
	    byte[] csvBytes = csvContent.toString().getBytes(StandardCharsets.UTF_8);
	    return Base64.getEncoder().encodeToString(csvBytes);
	}
	
//----------------excel format----------------------------------------
	
	@Loggable
	public String generateExcelBase64(String userId) throws IOException {
		List<CMUnderCEAtmDetailsDTO> atmDetailsDTO = getCMUnderCEAtmDetails(userId);
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("ATM Data");

			createHeaderRow(sheet);
			createDataRows(sheet, atmDetailsDTO);
			autoSizeColumns(sheet);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return Base64.getEncoder().encodeToString(outputStream.toByteArray());
		}
	}
	
	@Loggable
	public String generateExcelBase64AtmIndex(String userId) throws IOException {
		List<CMUnderCEAtmDetailsDTO2> atmDetailsDTO = getCMAtmDetailsExcelAndCSV(userId);
		System.out.println("download atm index details DTO: "+atmDetailsDTO);
		log.info("download atm index details DTO:{}",atmDetailsDTO);
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("ATM Data");

			createHeaderRow(sheet);
			createDataRowsAtmIndex(sheet, atmDetailsDTO);
			autoSizeColumns(sheet);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			return Base64.getEncoder().encodeToString(outputStream.toByteArray());
		}
	}

	private void createHeaderRow(Sheet sheet) {
		Row headerRow = sheet.createRow(0);
		String[] columns = { "ID", "ATM ID", "Bank Name", "Grade", "Address", "Machine Status", "Open Tickets",
				"Error Category", "Ownership", "Ticket Aging", "Transaction Trend", "MTD Performance", "Uptime Trend",
				"MTD Uptime", "Name of Channel Executive", "Name of Secondary Channel Executive", "Last Visited On" };

		for (int i = 0; i < columns.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns[i]);
		}
	}

	private void createDataRows(Sheet sheet, List<CMUnderCEAtmDetailsDTO> atmDetailsDTO) {
		int rowNum = 1;
		for (CMUnderCEAtmDetailsDTO atm : atmDetailsDTO) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(atm.getId());
			row.createCell(1).setCellValue(atm.getAtmid());
			row.createCell(2).setCellValue(atm.getBankname());
			row.createCell(3).setCellValue(atm.getGrade());
			row.createCell(4).setCellValue(atm.getAddress());
			row.createCell(5).setCellValue(atm.getMachineStatus());
			row.createCell(6).setCellValue(atm.getOpenTickets());
			row.createCell(7).setCellValue(atm.getErrorCategory());
			row.createCell(8).setCellValue(atm.getOwnership());
			row.createCell(9).setCellValue(atm.getTicketAging());
			row.createCell(10).setCellValue(atm.getTransactionTrend());
			row.createCell(11).setCellValue(atm.getMtdPerformance());
			row.createCell(12).setCellValue(atm.getUptimeTrend());
			row.createCell(13).setCellValue(atm.getMtdUptime());
			row.createCell(14).setCellValue(atm.getNameOfChannelExecutive());
			row.createCell(15).setCellValue(atm.getNameOfSecondaryChannelExecutive());
			row.createCell(16).setCellValue(atm.getLastVisitedOn());
		}
	}
	
	private void createDataRowsAtmIndex(Sheet sheet, List<CMUnderCEAtmDetailsDTO2> atmDetailsDTO) {
		int rowNum = 1;
		for (CMUnderCEAtmDetailsDTO2 atm : atmDetailsDTO) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(atm.getId());
			row.createCell(1).setCellValue(atm.getAtmid());
			row.createCell(2).setCellValue(atm.getBankname());
			row.createCell(3).setCellValue(atm.getGrade());
			row.createCell(4).setCellValue(atm.getAddress());
			row.createCell(5).setCellValue(atm.getMachineStatus());
			row.createCell(6).setCellValue(atm.getOpenTickets());
			row.createCell(7).setCellValue(atm.getErrorCategory());
			row.createCell(8).setCellValue(atm.getOwnership());
			row.createCell(9).setCellValue(atm.getTicketAging());
			row.createCell(10).setCellValue(atm.getTransactionTrend());
			row.createCell(11).setCellValue(atm.getMtdPerformance());
			row.createCell(12).setCellValue(atm.getUptimeTrend());
			row.createCell(13).setCellValue(atm.getMtdUptime());
			row.createCell(14).setCellValue(atm.getNameOfChannelExecutive());
			row.createCell(15).setCellValue(atm.getNameOfSecondaryChannelExecutive());
			row.createCell(16).setCellValue(atm.getLastVisitedOn());
		}
	}

	private void autoSizeColumns(Sheet sheet) {
		for (int i = 0; i < 17; i++) {
			sheet.autoSizeColumn(i);
		}
	}

}
