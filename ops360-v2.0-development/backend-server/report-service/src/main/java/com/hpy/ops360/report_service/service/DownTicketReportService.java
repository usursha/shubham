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
import com.hpy.ops360.report_service.dto.DownTicketData;
import com.hpy.ops360.report_service.dto.DownTicketReportDTO;
import com.hpy.ops360.report_service.dto.DownTicketReportDownloadRequest;
import com.hpy.ops360.report_service.dto.DownTicketReportRequest;
import com.hpy.ops360.report_service.dto.DownTicketReportRequestSearch;
import com.hpy.ops360.report_service.entity.DownTicketReportEntity;
import com.hpy.ops360.report_service.repository.DownTicketReportRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DownTicketReportService {

	@Autowired
	private DownTicketReportRepository repository;

	@Autowired
	private Helper helper;

	public DownTicketData getReport(DownTicketReportRequest request) {
		log.info("Fetching down ticket report for Manager: {}, CE: {}, Date Range: {} to {}", helper.getLoggedInUser(),
				request.getCeFullName(), request.getStartDate(), request.getEndDate());

		log.debug(
				"Additional filters - ATMID: {}, Bank: {}, Status: {}, TicketNumber: {}, Owner: {}, SubCallType: {}, BusinessModel: {}, SiteType: {}, EtaDateTime: {}",
				request.getAtmId(), request.getBank(), request.getStatus(), request.getTicketNumber(),
				request.getOwner(), request.getSubCallType(), request.getBusinessModel(), request.getSiteType(),
				request.getEtaDateTime());
		Integer realstatus = null;
		if (request.getStatus() == "Open") {
			realstatus = 1;
		} else if (request.getStatus() == "Close") {
			realstatus = 0;
		}
		DownTicketData data = new DownTicketData();
		if (request.getSortby().equalsIgnoreCase("Latest - Earliest")) {
			request.setSortby("DESC");
		} else {
			request.setSortby("ASC");
		}
		// validation for manager name, startdate and enddate
		if (helper.getLoggedInUser() == null || helper.getLoggedInUser().isEmpty()) {
			log.error("Manager name is required");
			throw new IllegalArgumentException("Manager name is required");
		}
		if (request.getStartDate() == null || request.getStartDate().isEmpty() || request.getEndDate() == null
				|| request.getEndDate().isEmpty()) {
			log.error("Start date and end date are required");
			throw new IllegalArgumentException("Start date and end date are required");
		}

		// Fetching the report data from the repository
		List<DownTicketReportEntity> entities = repository.getDownTicketReport(request.getStartDate(),
				request.getEndDate(), helper.getLoggedInUser(), request.getSearchkey(), request.getCeFullName(),
				request.getAtmId(), request.getBank(), realstatus, request.getTicketNumber(), request.getOwner(),
				request.getSubCallType(), request.getBusinessModel(), request.getSiteType(), request.getEtaDateTime(),
				request.getPageIndex(), request.getPageSize(), request.getSortby());
		log.info("Fetched {} records from the database", entities.size());
		List<DownTicketReportDTO> response = entities.stream().map(this::mapToDto).collect(Collectors.toList());

		if (entities.isEmpty()) {
			data.setTotalCounts(0);
		} else {
			data.setTotalCounts(entities.get(0).getTotalCount());
		}
		data.setData(response);
		return data;
	}

	public List<DownTicketReportDTO> getReportDownload(DownTicketReportDownloadRequest request) {
		log.info("Fetching down ticket report for Manager: {}, CE: {}, Date Range: {} to {}", helper.getLoggedInUser(),
				request.getCeFullName(), request.getStartDate(), request.getEndDate());

		log.debug(
				"Additional filters - ATMID: {}, Bank: {}, Status: {}, TicketNumber: {}, Owner: {}, SubCallType: {}, BusinessModel: {}, SiteType: {}, EtaDateTime: {}",
				request.getAtmId(), request.getBank(), request.getStatus(), request.getTicketNumber(),
				request.getOwner(), request.getSubCallType(), request.getBusinessModel(), request.getSiteType(),
				request.getEtaDateTime());
		Integer realstatus = null;
		if (request.getStatus() == "Open") {
			realstatus = 1;
		} else if (request.getStatus() == "Close") {
			realstatus = 0;
		}
		if (request.getSortby().equalsIgnoreCase("Latest - Earliest")) {
			request.setSortby("DESC");
		} else {
			request.setSortby("ASC");
		}
		// validation for manager name, startdate and enddate
		if (helper.getLoggedInUser() == null || helper.getLoggedInUser().isEmpty()) {
			log.error("Manager name is required");
			throw new IllegalArgumentException("Manager name is required");
		}
		if (request.getStartDate() == null || request.getStartDate().isEmpty() || request.getEndDate() == null
				|| request.getEndDate().isEmpty()) {
			log.error("Start date and end date are required");
			throw new IllegalArgumentException("Start date and end date are required");
		}

		// Fetching the report data from the repository
		List<DownTicketReportEntity> entities = repository.getDownTicketReport(request.getStartDate(),
				request.getEndDate(), helper.getLoggedInUser(), request.getSearchkey(), request.getCeFullName(),
				request.getAtmId(), request.getBank(), realstatus, request.getTicketNumber(), request.getOwner(),
				request.getSubCallType(), request.getBusinessModel(), request.getSiteType(), request.getEtaDateTime(),
				null, null, request.getSortby());
		log.info("Fetched {} records from the database", entities.size());
		List<DownTicketReportDTO> response = entities.stream().map(this::mapToDto).collect(Collectors.toList());

		return response;
	}

	public List<String> getReportSearch(DownTicketReportRequestSearch request) {
		log.info("Fetching down ticket report for Manager: {}, CE: {}, Date Range: {} to {}", helper.getLoggedInUser(),
				request.getCeFullName(), request.getStartDate(), request.getEndDate());

		log.debug(
				"Additional filters - ATMID: {}, Bank: {}, Status: {}, TicketNumber: {}, Owner: {}, SubCallType: {}, BusinessModel: {}, SiteType: {}, EtaDateTime: {}",
				request.getAtmId(), request.getBank(), request.getStatus(), request.getTicketNumber(),
				request.getOwner(), request.getSubCallType(), request.getBusinessModel(), request.getSiteType(),
				request.getEtaDateTime());
		if (request.getSortby().equalsIgnoreCase("Latest - Earliest")) {
			request.setSortby("DESC");
		} else {
			request.setSortby("ASC");
		}

		Integer realstatus = null;
		if (request.getStatus() == "Open") {
			realstatus = 1;
		} else if (request.getStatus() == "Close") {
			realstatus = 0;
		}
		// validation for manager name, startdate and enddate
		if (helper.getLoggedInUser() == null || helper.getLoggedInUser().isEmpty()) {
			log.error("Manager name is required");
			throw new IllegalArgumentException("Manager name is required");
		}
		if (request.getStartDate() == null || request.getStartDate().isEmpty() || request.getEndDate() == null
				|| request.getEndDate().isEmpty()) {
			log.error("Start date and end date are required");
			throw new IllegalArgumentException("Start date and end date are required");
		}

		// Fetching the report data from the repository
		List<DownTicketReportEntity> entities = repository.getDownTicketReport(request.getStartDate(),
				request.getEndDate(), helper.getLoggedInUser(), request.getSearchkey(), request.getCeFullName(),
				request.getAtmId(), request.getBank(), realstatus, request.getTicketNumber(), request.getOwner(),
				request.getSubCallType(), request.getBusinessModel(), request.getSiteType(), request.getEtaDateTime(),
				null, null, request.getSortby());
		log.info("Fetched {} records from the database", entities.size());
		List<String> filteredTicketId = entities.stream().map(DownTicketReportEntity::getTicketnumber)
				.filter(ticketnumber -> ticketnumber != null
						&& ticketnumber.toLowerCase().contains(request.getSearchkey().toLowerCase()))
				.collect(Collectors.toList());
		return filteredTicketId;
	}

	public String writeExcel(List<DownTicketReportDTO> data) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Down Tickets");

			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("ATM ID");
			header.createCell(1).setCellValue("Business Category");
			header.createCell(2).setCellValue("Site Type");
			header.createCell(3).setCellValue("Bank");
			header.createCell(4).setCellValue("Status");
			header.createCell(5).setCellValue("Ticket Number");
			header.createCell(6).setCellValue("Downtime (Hours)");
			header.createCell(7).setCellValue("Downtime Bucket");
			header.createCell(8).setCellValue("Vendor");
			header.createCell(9).setCellValue("Owner");
			header.createCell(10).setCellValue("Sub Call Type");
			header.createCell(11).setCellValue("Customer Remark");
			header.createCell(12).setCellValue("CE Full Name");
			header.createCell(13).setCellValue("CM Full Name");
			header.createCell(14).setCellValue("Created DateTime");
			header.createCell(15).setCellValue("CE ETA DateTime");
			header.createCell(16).setCellValue("CE Action DateTime");

			int rowNum = 1;
			for (DownTicketReportDTO dto : data) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(dto.getAtmid());
				row.createCell(1).setCellValue(dto.getBusinesscategory());
				row.createCell(2).setCellValue(dto.getSitetype());
				row.createCell(3).setCellValue(dto.getBank());
				row.createCell(4).setCellValue(dto.getStatus());
				row.createCell(5).setCellValue(dto.getTicketnumber());
				row.createCell(6).setCellValue(dto.getDowntimeinhrs());
				row.createCell(7).setCellValue(dto.getDowntimebucket());
				row.createCell(8).setCellValue(dto.getVendor());
				row.createCell(9).setCellValue(dto.getOwner());
				row.createCell(10).setCellValue(dto.getSubcalltype());
				row.createCell(11).setCellValue(dto.getCustomerremark());
				row.createCell(12).setCellValue(dto.getCefullname());
				row.createCell(13).setCellValue(dto.getCmfullname());
				row.createCell(14).setCellValue(dto.getCreateddatetime());
				row.createCell(15).setCellValue(dto.getCeetadatetime());
				row.createCell(16).setCellValue(dto.getCeactiondatetime());

			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);

			workbook.close();

			String base64Excel = Base64.getEncoder().encodeToString(bos.toByteArray());
			return base64Excel;
		}
	}

	private DownTicketReportDTO mapToDto(DownTicketReportEntity entity) {
		DownTicketReportDTO dto = new DownTicketReportDTO();
		dto.setAtmid(entity.getAtmid());
		dto.setBusinesscategory(entity.getBusinesscategory());
		dto.setSitetype(entity.getSitetype());
		dto.setBank(entity.getBank());
		dto.setStatus(entity.getStatus());
		dto.setTicketnumber(entity.getTicketnumber());
		dto.setDowntimeinhrs(entity.getDowntimeinhrs());
		dto.setDowntimebucket(entity.getDowntimebucket());
		dto.setVendor(entity.getVendor());
		dto.setOwner(entity.getOwner());
		dto.setSubcalltype(entity.getSubcalltype());
		dto.setCustomerremark(entity.getCustomerremark());
		dto.setCefullname(entity.getCefullname());
		dto.setCmfullname(entity.getCmfullname());
		dto.setCreateddatetime(entity.getCreateddatetime());
		dto.setCeetadatetime(entity.getCeetadatetime());
		dto.setCeactiondatetime(entity.getCeactiondatetime());
		return dto;
	}
}