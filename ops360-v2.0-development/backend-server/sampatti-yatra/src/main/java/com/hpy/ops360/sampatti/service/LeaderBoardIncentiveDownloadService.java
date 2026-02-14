package com.hpy.ops360.sampatti.service;

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

import com.hpy.ops360.sampatti.dto.LeaderBoardIncentiveDownloadDto;
import com.hpy.ops360.sampatti.dto.response.GenericResponseDto;
import com.hpy.ops360.sampatti.entity.LeaderBoardIncentiveDownloadEntity;

import com.hpy.ops360.sampatti.repository.LeaderBoardIncentiveDownloadRepo;

import com.hpy.ops360.sampatti.util.CMInsentiveCSVGenerator;
import com.hpy.ops360.sampatti.util.CMInsentiveExcelGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LeaderBoardIncentiveDownloadService {

	@Autowired
	private CMInsentiveExcelGenerator excelGenerator;

	@Autowired
	private CMInsentiveCSVGenerator csvGenerator;

	@Autowired
	private LeaderBoardIncentiveDownloadRepo incentiveDownloadRepo;

	public GenericResponseDto getDataExcel(String monthYear, String userType) {
		GenericResponseDto response = new GenericResponseDto();
		log.info("Enter inside getDataExcel Method");

		try {
			XSSFWorkbook workbook = generateCMIncentiveExcel(monthYear, userType);
//			if (workbook == null) {
//			    response.setMessage("Error generating CM incentive Excel file");
//			    return response;
//			}
			
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

			log.info("CM incentive Excel file encoded to Base64 and sent in response.");
			response.setMessage(base64Excel);
			return response;
		} catch (IOException e) {
			log.error("Error generating CM incentive Excel file", e);
			response.setMessage("Error generating CM incentive Excel file");
			return response;
		}

	}

	private XSSFWorkbook generateCMIncentiveExcel(String monthYear, String userType) {

		List<LeaderBoardIncentiveDownloadEntity> reportEntities = incentiveDownloadRepo
				.getCmIncentiveMonthlyData(monthYear, userType);
//		if (reportEntities == null || reportEntities.isEmpty()) {
//	        log.warn("No incentive data found for month: {}, userType: {}", monthYear, userType);
//	        return excelGenerator.generateEmptyExcelWithMessage("No data found for userType: " + userType + " in " + monthYear);
//	    }

		// Handle empty or null data directly
	    if (reportEntities == null || reportEntities.isEmpty()) {
	        XSSFWorkbook workbook = new XSSFWorkbook();
	        XSSFSheet sheet = workbook.createSheet("Incentive Data");
	        Row header = sheet.createRow(0);
	        Cell cell = header.createCell(0);
	        cell.setCellValue("No data found for userType: " + userType + " in " + monthYear);
	        return workbook;
	    }
		
		log.info("Fetched incentive data for month: {}, userType: {}, records: {}", monthYear, userType,
				reportEntities.size());

		List<LeaderBoardIncentiveDownloadDto> data = reportEntities.stream()
				.map(result -> new LeaderBoardIncentiveDownloadDto(result.getRank(), result.getFullName(),
						result.getRole(), result.getTarget(), result.getAchieved(), result.getOverageShortage(),
						result.getIncentiveAmount(), result.getChannelManager(), result.getStateChannelManager(),
						result.getZonalHead()))
				.toList();

		return switch (userType.toUpperCase()) {
		case "CE" -> excelGenerator.generateExcelCE(data);
		case "CM" -> excelGenerator.generateExcelCM(data);
		case "SCM" -> excelGenerator.generateExcelSCM(data);
		case "ZH" -> excelGenerator.generateExcelRCM(data);
		default -> {
			log.warn("Unsupported userType: {}", userType);
			yield null;
		}
		};
	}

	public GenericResponseDto getDataCSV(String monthYear, String userType) {

		GenericResponseDto response = new GenericResponseDto();
		log.info("Enter inside getDataCSV Method");

		try {
			// Generate the CSV string (base64 encoded)
			String base64CSV = generateCMIncentiveCSV(monthYear, userType);

			log.info("CM incentive CSV file encoded to Base64 and sent in response.");
			response.setMessage(base64CSV);
			return response;
		} catch (IOException e) {
			log.error("Error generating CM incentive CSV file", e);
			response.setMessage("Error generating CM incentive CSV file");
			return response;
		}

	}

	private String generateCMIncentiveCSV(String monthYear, String userType) throws IOException {

		List<LeaderBoardIncentiveDownloadEntity> reportEntities = incentiveDownloadRepo.getCmIncentiveMonthlyData(monthYear, userType);
		if (reportEntities == null || reportEntities.isEmpty()) {
	        log.warn("No incentive data found for month: {}, userType: {}", monthYear, userType);
	        return "No data found for userType: " + userType + " in " + monthYear;
	    }

		log.info("Fetched incentive data for month: {}, userType: {}, records: {}", monthYear, userType,reportEntities.size());

		List<LeaderBoardIncentiveDownloadDto> data = reportEntities.stream()
				.map(result -> new LeaderBoardIncentiveDownloadDto(result.getRank(), result.getFullName(),
						result.getRole(), result.getTarget(), result.getAchieved(), result.getOverageShortage(),
						result.getIncentiveAmount(), result.getChannelManager(), result.getStateChannelManager(),
						result.getZonalHead()))
				.toList();

		return switch (userType.toUpperCase()) {
		case "CE" -> csvGenerator.generateCSVCE(data);
		case "CM" -> csvGenerator.generateCSVCM(data);
		case "SCM" -> csvGenerator.generateCSVSCM(data);
		case "ZH" -> csvGenerator.generateCSVRCM(data);
		default -> {
			log.warn("Unsupported userType: {}", userType);
			yield null;
		}
		};
	}

}
