package com.hpy.ops360.ticketing.cm.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.TicketHistoryArchiveDto;
import com.hpy.ops360.ticketing.cm.dto.TicketHistoryArchiveRequestDto;
import com.hpy.ops360.ticketing.cm.dto.TicketHistoryArchiveResponseDto;
import com.hpy.ops360.ticketing.cm.entity.TicketHistoryArchiveEntity;
import com.hpy.ops360.ticketing.cm.repo.TicketHistoryArchiveRepository;
import com.hpy.ops360.ticketing.dto.GenericResponseDto;
import com.hpy.ops360.ticketing.dto.GenericResponseDto2;
import com.hpy.ops360.ticketing.util.TicketHistoryArchiveExcelGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketHistoryArchiveService {

	@Autowired
	private TicketHistoryArchiveRepository ticketHistoryArchiveRepository;
	
	@Autowired
	private TicketHistoryArchiveExcelGenerator archiveExcelGenerator;
	
	
	
	
	  public GenericResponseDto2 getHistoryArchiveData(TicketHistoryArchiveRequestDto request) {
	        log.info("Enter inside getHistoryArchiveData Method");
	        GenericResponseDto2 genericResponse = new GenericResponseDto2();
	        TicketHistoryArchiveResponseDto response = new TicketHistoryArchiveResponseDto();

	        try {
	            // Generate Excel data
	            XSSFWorkbook workbook = generateTicketHistoryArchExcel(request);
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            workbook.write(bos);
	            workbook.close();
	            String base64Excel = Base64.getEncoder().encodeToString(bos.toByteArray());
	            
	            TicketHistoryArchiveResponseDto.ExcelData excelData = new TicketHistoryArchiveResponseDto.ExcelData();
	            excelData.setBase64(base64Excel);
	            excelData.setDownloadLink(generateTicketHistoryArchExcelURL(request));
	            response.setExcelData(excelData);

	            // Generate CSV data
	            String base64CSV = generateTicketArchiveCSV(request);
	            TicketHistoryArchiveResponseDto.CsvData csvData = new TicketHistoryArchiveResponseDto.CsvData();
	            csvData.setBase64(base64CSV);
	            csvData.setDownloadLink(generateTicketArchiveCSVURL(request));
	            response.setCsvData(csvData);

	            genericResponse.setStatusCode("Success");
	            genericResponse.setMessage("Excel and CSV files generated successfully.");
	            genericResponse.setData(response);
	            
	            log.info("Ticket History Excel and CSV files encoded to Base64 and sent in response.");
	            return genericResponse;
	        } catch (IOException e) {
	            log.error("Error generating Ticket History files", e);
	            genericResponse.setMessage("Error generating Ticket History files");
	            genericResponse.setStatusCode("Error");
	            return genericResponse;
	        }
	    }
	
	
	
	
	
	public GenericResponseDto getHistoryArchiveDataExcel(TicketHistoryArchiveRequestDto request) {
		GenericResponseDto response = new GenericResponseDto();
		log.info("Enter inside getHistoryArchiveDataExcel Method");

		try {
			XSSFWorkbook workbook = generateTicketHistoryArchExcel(request);
			
			// Check if workbook is empty or contains "No data found" marker
	        XSSFSheet sheet = workbook.getSheetAt(0);
	        Row firstRow = sheet.getRow(0);
	        Cell firstCell = firstRow != null ? firstRow.getCell(0) : null;
	        String marker = firstCell != null ? firstCell.getStringCellValue() : "";

	        if (marker.startsWith("No data found") || marker.startsWith("Unsupported user type")) {
	            response.setMessage(marker); // Send readable message instead of Base64
	            return response;
	        }
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			workbook.close();

			String base64Excel = Base64.getEncoder().encodeToString(bos.toByteArray());

			log.info("Ticket History Excel file encoded to Base64 and sent in response.");
			response.setStatusCode("Success");
			response.setMessage(base64Excel);
			return response;
		} catch (IOException e) {
			log.error("Error generating Ticket History Excel file", e);
			response.setMessage("Error generating Ticket History Excel file");
			return response;
		}

	}

	private XSSFWorkbook generateTicketHistoryArchExcel(TicketHistoryArchiveRequestDto request) {

		List<TicketHistoryArchiveEntity> reportEntities = ticketHistoryArchiveRepository
				.getTicketHistoryArchiveData(request.getStartDate(), request.getEndDate(), request.getBankName(), request.getAtmId(), request.getTicketNo(), request.getUserId());

		log.info("Ticket History Archite data: {}", reportEntities);
				
		// Handle empty or null data directly
	    if (reportEntities == null || reportEntities.isEmpty()) {
	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("Ticket History Archive Data");
	        Row header = sheet.createRow(0);
	        Cell cell = header.createCell(0);
	        cell.setCellValue("No data found for Ticket History Data: "+ request );
	        return workbook;
	    }
		
		log.info("Ticket History Archite data: {}",reportEntities,reportEntities.size());

	    List<TicketHistoryArchiveDto> data = reportEntities.stream()
	    	    .map(result -> new TicketHistoryArchiveDto(
	    	        result.getSrno() != null ? result.getSrno() : "", 
	    	        result.getCustomer() != null ? result.getCustomer() : "", 
	    	        result.getEquipmentId() != null ? result.getEquipmentId() : "",
	    	        result.getModel() != null ? result.getModel() : "", 
	    	        result.getAtmCategory() != null ? result.getAtmCategory() : "", 
	    	        result.getAtmClassification() != null ? result.getAtmClassification() : "", 
	    	        result.getCallDate() != null ? result.getCallDate().toString() : "", // Convert Date to String if necessary
	    	        result.getCreatedDate() != null ? result.getCreatedDate().toString() : "", // Convert Date to String if necessary
	    	        result.getCallType() != null ? result.getCallType() : "", 
	    	        result.getSubCallType() != null ? result.getSubCallType() : "", 
	    	        result.getCompletionDateWithTime() != null ? result.getCompletionDateWithTime().toString() : "", // Convert Date to String if necessary
	    	        result.getDowntimeInMins() != null ? result.getDowntimeInMins() : 0, 
	    	        result.getVendor() != null ? result.getVendor() : "", 
	    	        result.getServiceCode() != null ? result.getServiceCode() : "", 
	    	        result.getDiagnosis() != null ? result.getDiagnosis() : "", 
	    	        result.getEventCode() != null ? result.getEventCode() : "", 
	    	        result.getHelpdeskName() != null ? result.getHelpdeskName() : "", 
	    	        result.getLastAllocatedTime() != null ? result.getLastAllocatedTime().toString() : "", // Convert Date to String if necessary
	    	        result.getLastComment() != null ? result.getLastComment() : "", 
	    	        result.getLastActivity() != null ? result.getLastActivity() : "", 
	    	        result.getStatus() != null ? result.getStatus() : "", 
	    	        result.getSubStatus() != null ? result.getSubStatus() : "", 
	    	        result.getRo() != null ? result.getRo() : "", 
	    	        result.getSite() != null ? result.getSite() : "", 
	    	        result.getAddress() != null ? result.getAddress() : "", 
	    	        result.getCity() != null ? result.getCity() : "",
	    	        result.getLocationName() != null ? result.getLocationName() : "", 
	    	        result.getState() != null ? result.getState() : "", 
	    	        result.getNextFollowUp() != null ? result.getNextFollowUp().toString() : "", // Convert Date to String if necessary
	    	        result.getEtaDateTime() != null ? result.getEtaDateTime().toString() : "", // Convert Date to String if necessary
	    	        result.getOwner() != null ? result.getOwner() : "", // Handle null
	    	        result.getCustomerRemark() != null ? result.getCustomerRemark() : "" 
	    	    //    result.getInsertDate() != null ? result.getInsertDate().toString() : "" // Convert Date to String if necessary
	    	    ))
	    	    .toList();

	    XSSFWorkbook excelData = archiveExcelGenerator.generateExcel(data);

		return excelData;
	}
	
	
	private String generateTicketHistoryArchExcelURL(TicketHistoryArchiveRequestDto request) {

		List<TicketHistoryArchiveEntity> reportEntities = ticketHistoryArchiveRepository
				.getTicketHistoryArchiveData(request.getStartDate(), request.getEndDate(), request.getBankName(), request.getAtmId(), request.getTicketNo(), request.getUserId());

		log.info("Ticket History Archite data: {}", reportEntities);
		
		log.info("Ticket History Archite data: {}",reportEntities,reportEntities.size());

	    List<TicketHistoryArchiveDto> data = reportEntities.stream()
	    	    .map(result -> new TicketHistoryArchiveDto(
	    	        result.getSrno() != null ? result.getSrno() : "", 
	    	        result.getCustomer() != null ? result.getCustomer() : "", 
	    	        result.getEquipmentId() != null ? result.getEquipmentId() : "",
	    	        result.getModel() != null ? result.getModel() : "", 
	    	        result.getAtmCategory() != null ? result.getAtmCategory() : "", 
	    	        result.getAtmClassification() != null ? result.getAtmClassification() : "", 
	    	        result.getCallDate() != null ? result.getCallDate().toString() : "", // Convert Date to String if necessary
	    	        result.getCreatedDate() != null ? result.getCreatedDate().toString() : "", // Convert Date to String if necessary
	    	        result.getCallType() != null ? result.getCallType() : "", 
	    	        result.getSubCallType() != null ? result.getSubCallType() : "", 
	    	        result.getCompletionDateWithTime() != null ? result.getCompletionDateWithTime().toString() : "", // Convert Date to String if necessary
	    	        result.getDowntimeInMins() != null ? result.getDowntimeInMins() : 0, 
	    	        result.getVendor() != null ? result.getVendor() : "", 
	    	        result.getServiceCode() != null ? result.getServiceCode() : "", 
	    	        result.getDiagnosis() != null ? result.getDiagnosis() : "", 
	    	        result.getEventCode() != null ? result.getEventCode() : "", 
	    	        result.getHelpdeskName() != null ? result.getHelpdeskName() : "", 
	    	        result.getLastAllocatedTime() != null ? result.getLastAllocatedTime().toString() : "", // Convert Date to String if necessary
	    	        result.getLastComment() != null ? result.getLastComment() : "", 
	    	        result.getLastActivity() != null ? result.getLastActivity() : "", 
	    	        result.getStatus() != null ? result.getStatus() : "", 
	    	        result.getSubStatus() != null ? result.getSubStatus() : "", 
	    	        result.getRo() != null ? result.getRo() : "", 
	    	        result.getSite() != null ? result.getSite() : "", 
	    	        result.getAddress() != null ? result.getAddress() : "", 
	    	        result.getCity() != null ? result.getCity() : "",
	    	        result.getLocationName() != null ? result.getLocationName() : "", 
	    	        result.getState() != null ? result.getState() : "", 
	    	        result.getNextFollowUp() != null ? result.getNextFollowUp().toString() : "", // Convert Date to String if necessary
	    	        result.getEtaDateTime() != null ? result.getEtaDateTime().toString() : "", // Convert Date to String if necessary
	    	        result.getOwner() != null ? result.getOwner() : "", // Handle null
	    	        result.getCustomerRemark() != null ? result.getCustomerRemark() : "" 
	    	    //    result.getInsertDate() != null ? result.getInsertDate().toString() : "" // Convert Date to String if necessary
	    	    ))
	    	    .toList();

	    String excelData = archiveExcelGenerator.exportToexcelURL(data);

		return excelData;
	}

	
	public GenericResponseDto getHistoryArchiveDataCsv(TicketHistoryArchiveRequestDto request) {

		GenericResponseDto response = new GenericResponseDto();
		log.info("Enter inside getHistoryArchiveDataCsv Method");

		try {
			// Generate the CSV string (base64 encoded)
			String base64CSV = generateTicketArchiveCSV(request);

			log.info("Ticket History CSV file encoded to Base64 and sent in response.");
			response.setMessage(base64CSV);
			return response;
		} catch (IOException e) {
			log.error("Error generating CM incentive CSV file", e);
			response.setMessage("Error generating CM incentive CSV file");
			return response;
		}

	}
	
	private String generateTicketArchiveCSV(TicketHistoryArchiveRequestDto request) throws IOException {

		List<TicketHistoryArchiveEntity> reportEntities = ticketHistoryArchiveRepository
				.getTicketHistoryArchiveData(request.getStartDate(), request.getEndDate(), request.getBankName(), request.getAtmId(), request.getTicketNo(), request.getUserId());

		log.info("Ticket History Archite data: {}", reportEntities);
		
		if (reportEntities == null || reportEntities.isEmpty()) {
	        log.warn("No Ticket Archive data : {}",reportEntities);
	        return "No Data Found";
	    }

		log.info("Ticket History Archite data: {}",reportEntities,reportEntities.size());

	    List<TicketHistoryArchiveDto> data = reportEntities.stream()
	    	    .map(result -> new TicketHistoryArchiveDto(
	    	        result.getSrno() != null ? result.getSrno() : "", 
	    	        result.getCustomer() != null ? result.getCustomer() : "", 
	    	        result.getEquipmentId() != null ? result.getEquipmentId() : "",
	    	        result.getModel() != null ? result.getModel() : "", 
	    	        result.getAtmCategory() != null ? result.getAtmCategory() : "", 
	    	        result.getAtmClassification() != null ? result.getAtmClassification() : "", 
	    	        result.getCallDate() != null ? result.getCallDate().toString() : "", // Convert Date to String if necessary
	    	        result.getCreatedDate() != null ? result.getCreatedDate().toString() : "", // Convert Date to String if necessary
	    	        result.getCallType() != null ? result.getCallType() : "", 
	    	        result.getSubCallType() != null ? result.getSubCallType() : "", 
	    	        result.getCompletionDateWithTime() != null ? result.getCompletionDateWithTime().toString() : "", // Convert Date to String if necessary
	    	        result.getDowntimeInMins() != null ? result.getDowntimeInMins() : 0, 
	    	        result.getVendor() != null ? result.getVendor() : "", 
	    	        result.getServiceCode() != null ? result.getServiceCode() : "", 
	    	        result.getDiagnosis() != null ? result.getDiagnosis() : "", 
	    	        result.getEventCode() != null ? result.getEventCode() : "", 
	    	        result.getHelpdeskName() != null ? result.getHelpdeskName() : "", 
	    	        result.getLastAllocatedTime() != null ? result.getLastAllocatedTime().toString() : "", // Convert Date to String if necessary
	    	        result.getLastComment() != null ? result.getLastComment() : "", 
	    	        result.getLastActivity() != null ? result.getLastActivity() : "", 
	    	        result.getStatus() != null ? result.getStatus() : "", 
	    	        result.getSubStatus() != null ? result.getSubStatus() : "", 
	    	        result.getRo() != null ? result.getRo() : "", 
	    	        result.getSite() != null ? result.getSite() : "", 
	    	        result.getAddress() != null ? result.getAddress() : "", 
	    	        result.getCity() != null ? result.getCity() : "",
	    	        result.getLocationName() != null ? result.getLocationName() : "", 
	    	        result.getState() != null ? result.getState() : "", 
	    	        result.getNextFollowUp() != null ? result.getNextFollowUp().toString() : "", // Convert Date to String if necessary
	    	        result.getEtaDateTime() != null ? result.getEtaDateTime().toString() : "", // Convert Date to String if necessary
	    	        result.getOwner() != null ? result.getOwner() : "", // Handle null
	    	        result.getCustomerRemark() != null ? result.getCustomerRemark() : ""
	    	  //      result.getInsertDate() != null ? result.getInsertDate().toString() : "" // Convert Date to String if necessary
	    	    ))
	    	    .toList();

	    String CSVdata = archiveExcelGenerator.generateCSV(data);

		return CSVdata;
	}
	
	
	private String generateTicketArchiveCSVURL(TicketHistoryArchiveRequestDto request) throws IOException {

		List<TicketHistoryArchiveEntity> reportEntities = ticketHistoryArchiveRepository
				.getTicketHistoryArchiveData(request.getStartDate(), request.getEndDate(), request.getBankName(), request.getAtmId(), request.getTicketNo(), request.getUserId());

		log.info("Ticket History Archite data: {}", reportEntities);
		
		if (reportEntities == null || reportEntities.isEmpty()) {
	        log.warn("No Ticket Archive data : {}",reportEntities);
	        return "No Data Found";
	    }

		log.info("Ticket History Archite data: {}",reportEntities,reportEntities.size());

	    List<TicketHistoryArchiveDto> data = reportEntities.stream()
	    	    .map(result -> new TicketHistoryArchiveDto(
	    	        result.getSrno() != null ? result.getSrno() : "", 
	    	        result.getCustomer() != null ? result.getCustomer() : "", 
	    	        result.getEquipmentId() != null ? result.getEquipmentId() : "",
	    	        result.getModel() != null ? result.getModel() : "", 
	    	        result.getAtmCategory() != null ? result.getAtmCategory() : "", 
	    	        result.getAtmClassification() != null ? result.getAtmClassification() : "", 
	    	        result.getCallDate() != null ? result.getCallDate().toString() : "", // Convert Date to String if necessary
	    	        result.getCreatedDate() != null ? result.getCreatedDate().toString() : "", // Convert Date to String if necessary
	    	        result.getCallType() != null ? result.getCallType() : "", 
	    	        result.getSubCallType() != null ? result.getSubCallType() : "", 
	    	        result.getCompletionDateWithTime() != null ? result.getCompletionDateWithTime().toString() : "", // Convert Date to String if necessary
	    	        result.getDowntimeInMins() != null ? result.getDowntimeInMins() : 0, 
	    	        result.getVendor() != null ? result.getVendor() : "", 
	    	        result.getServiceCode() != null ? result.getServiceCode() : "", 
	    	        result.getDiagnosis() != null ? result.getDiagnosis() : "", 
	    	        result.getEventCode() != null ? result.getEventCode() : "", 
	    	        result.getHelpdeskName() != null ? result.getHelpdeskName() : "", 
	    	        result.getLastAllocatedTime() != null ? result.getLastAllocatedTime().toString() : "", // Convert Date to String if necessary
	    	        result.getLastComment() != null ? result.getLastComment() : "", 
	    	        result.getLastActivity() != null ? result.getLastActivity() : "", 
	    	        result.getStatus() != null ? result.getStatus() : "", 
	    	        result.getSubStatus() != null ? result.getSubStatus() : "", 
	    	        result.getRo() != null ? result.getRo() : "", 
	    	        result.getSite() != null ? result.getSite() : "", 
	    	        result.getAddress() != null ? result.getAddress() : "", 
	    	        result.getCity() != null ? result.getCity() : "",
	    	        result.getLocationName() != null ? result.getLocationName() : "", 
	    	        result.getState() != null ? result.getState() : "", 
	    	        result.getNextFollowUp() != null ? result.getNextFollowUp().toString() : "", // Convert Date to String if necessary
	    	        result.getEtaDateTime() != null ? result.getEtaDateTime().toString() : "", // Convert Date to String if necessary
	    	        result.getOwner() != null ? result.getOwner() : "", // Handle null
	    	        result.getCustomerRemark() != null ? result.getCustomerRemark() : ""
	    	  //      result.getInsertDate() != null ? result.getInsertDate().toString() : "" // Convert Date to String if necessary
	    	    ))
	    	    .toList();

	    String CSVdata = archiveExcelGenerator.exportToCsvURL(data);

		return CSVdata;
	}

}

