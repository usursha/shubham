package com.hpy.ops360.report_service.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.report_service.config.Helper;
import com.hpy.ops360.report_service.dto.AtmUptimeDTO;
import com.hpy.ops360.report_service.dto.AtmUptimeRequest;
import com.hpy.ops360.report_service.dto.AtmUptimeRequestSearch;
import com.hpy.ops360.report_service.dto.AtmUptimedownloadRequest;
import com.hpy.ops360.report_service.dto.AtmuptimeTicketData;
import com.hpy.ops360.report_service.entity.AtmUptimeEntity;
import com.hpy.ops360.report_service.repository.AtmUptimeReportRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AtmUptimeService {

	@Autowired
	private AtmUptimeReportRepository repository;

	@Autowired
	private Helper helper;

	public AtmuptimeTicketData getUptimeReport(AtmUptimeRequest request) {
		String user = helper.getLoggedInUser();
		log.info("Fetching ATM uptime report for user: {}, with request parameters: {}", user, request);
		Integer realstatus = null;
		if ("Operational".equals(request.getStatus())) {
		    realstatus = 1;
		} else if ("Not Operational".equals(request.getStatus())) {
		    realstatus = 0;
		}


		AtmuptimeTicketData response = new AtmuptimeTicketData();
		List<AtmUptimeEntity> entities = repository.getAtmUptimeReport(user, request.getStartDate(),
				request.getEndDate(), request.getSearchkey(), request.getUptimeRange(), request.getLiveDate(),
				request.getAtmId(), request.getBankName(), request.getCity(), request.getSite(), request.getSiteId(),
				realstatus, request.getSortBy(), request.getPageNo(), request.getPageSize());
		log.info("Fetched {} records from the database", entities.size());
		log.info("Entiteis::-" + entities);
		List<AtmUptimeDTO> data = entities.stream().map(entity -> {
			AtmUptimeDTO dto = new AtmUptimeDTO();
			dto.setAtmId(entity.getAtmId());
			dto.setBankName(entity.getBankName());
			dto.setCity(entity.getCity());
			dto.setAddress(entity.getAddress());
			dto.setSite(entity.getSite());
			dto.setSiteId(entity.getSiteId());
			dto.setStatus(entity.getStatus());
			dto.setInstallationDate(entity.getInstallationDate());
			dto.setUptime(entity.getUptime());
			dto.setCeFullName(entity.getCeFullName());
			return dto;
		}).collect(Collectors.toList());
		log.info("Response:-" + response);
		if (entities.isEmpty()) {
			response.setTotalCounts(0);
		} else {
			response.setTotalCounts(entities.get(0).getTotalCount());
		}
		response.setData(data);
		return response;
	}
	
	
	public List<AtmUptimeDTO> getUptimeReportDownload(AtmUptimedownloadRequest request) {
		String user = helper.getLoggedInUser();
		log.info("Fetching ATM uptime report for user: {}, with request parameters: {}", user, request);
		Integer realstatus = null;
		if ("Operational".equals(request.getStatus())) {
		    realstatus = 1;
		} else if ("Not Operational".equals(request.getStatus())) {
		    realstatus = 0;
		}


		List<AtmUptimeEntity> entities = repository.getAtmUptimeReport(user, request.getStartDate(),
				request.getEndDate(), request.getSearchkey(), request.getUptimeRange(), request.getLiveDate(),
				request.getAtmId(), request.getBankName(), request.getCity(), request.getSite(), request.getSiteId(),
				realstatus, request.getSortBy(), null, null);
		log.info("Fetched {} records from the database", entities.size());
		log.info("Entiteis::-" + entities);
		List<AtmUptimeDTO> data = entities.stream().map(entity -> {
			AtmUptimeDTO dto = new AtmUptimeDTO();
			dto.setAtmId(entity.getAtmId());
			dto.setBankName(entity.getBankName());
			dto.setCity(entity.getCity());
			dto.setAddress(entity.getAddress());
			dto.setSite(entity.getSite());
			dto.setSiteId(entity.getSiteId());
			dto.setStatus(entity.getStatus());
			dto.setInstallationDate(entity.getInstallationDate());
			dto.setUptime(entity.getUptime());
			dto.setCeFullName(entity.getCeFullName());
			return dto;
		}).collect(Collectors.toList());
		
		return data;
	}

	public List<String> getUptimeReportSearch(AtmUptimeRequestSearch request) {
		String user = helper.getLoggedInUser();
		Integer realstatus = null;
		if ("Operational".equals(request.getStatus())) {
		    realstatus = 1;
		} else if ("Not Operational".equals(request.getStatus())) {
		    realstatus = 0;
		}

		log.info("Fetching ATM uptime report for user: {}, with request parameters: {}", user, request);
		log.info(realstatus + "realstatus");
		List<AtmUptimeEntity> entities = repository.getAtmUptimeReport(user, request.getStartDate(),
				request.getEndDate(), request.getSearchkey(), request.getUptimeRange(), request.getLiveDate(),
				request.getAtmId(), request.getBankName(), request.getCity(), request.getSite(), request.getSiteId(),
				realstatus, request.getSortBy(), null, null);
		log.info("Fetched {} records from the database", entities.size());
		log.info("Entiteis::-" + entities);

		List<String> filteredAtmIds = entities.stream().map(AtmUptimeEntity::getAtmId)
				.filter(atmid -> atmid != null && atmid.toLowerCase().contains(request.getAtmId().toLowerCase()))
				.collect(Collectors.toList());

		log.info("Filtered {} ATM IDs matching search key", filteredAtmIds.size());
		return filteredAtmIds;
	}
	
	
	
	public String writeExcel(List<AtmUptimeDTO> data) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("ATMUPtime Tickets");

			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("ATM ID");
			header.createCell(1).setCellValue("Bank Name");
			header.createCell(2).setCellValue("City");
			header.createCell(3).setCellValue("Address");
			header.createCell(4).setCellValue("Site");
			header.createCell(5).setCellValue("Site Id");
			header.createCell(6).setCellValue("Status");
			header.createCell(7).setCellValue("Installation Date");
			header.createCell(8).setCellValue("Uptime");
			header.createCell(9).setCellValue("CE Full NAME");

			int rowNum = 1;
			for (AtmUptimeDTO dto : data) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(dto.getAtmId());
				row.createCell(1).setCellValue(dto.getBankName());
				row.createCell(2).setCellValue(dto.getCity());
				row.createCell(3).setCellValue(dto.getAddress());
				row.createCell(4).setCellValue(dto.getSite());
				row.createCell(5).setCellValue(dto.getSiteId());
				row.createCell(6).setCellValue(dto.getStatus());
				row.createCell(7).setCellValue(dto.getInstallationDate());
				row.createCell(8).setCellValue(dto.getUptime());
				row.createCell(9).setCellValue(dto.getCeFullName());

			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);

			workbook.close();

			String base64Excel = Base64.getEncoder().encodeToString(bos.toByteArray());
			return base64Excel;
		}
	}
}
