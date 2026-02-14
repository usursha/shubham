package com.hpy.ops360.report_service.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.ops360.report_service.dto.UptimeReportResultDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UptimeTicketFileExportService {

	@Value("${ops360.assets.base-path}")
	private String BASE_PATH;

	@Value("${ops360.assets.base-url}")
	private String BASE_URL;

	public String exportToFile(List<UptimeReportResultDTO> data, String format) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
		String filename = "uptime_ticket_" + timestamp + (format.equalsIgnoreCase("excel") ? ".xlsx" : ".csv");
		String fullPath = BASE_PATH + filename;

		try {
			if (format.equalsIgnoreCase("excel")) {
				writeExcel(data, fullPath);
			} else if (format.equalsIgnoreCase("csv")) {
				writeCsv(data, fullPath);
			} else {
				throw new IllegalArgumentException("Unsupported format: " + format);
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to export file: " + e.getMessage(), e);
		}

		return BASE_URL + filename;
	}

	private void writeExcel(List<UptimeReportResultDTO> data, String path) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Uptime Tickets");

			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("Sr No");
			header.createCell(1).setCellValue("User Id");
			header.createCell(2).setCellValue("Transaction Achieved");
			header.createCell(3).setCellValue("Transaction Target");
			header.createCell(4).setCellValue("Uptime Target");
			header.createCell(5).setCellValue("Uptime Achieved");
			header.createCell(6).setCellValue("ATM Count");
			header.createCell(7).setCellValue("Full Name");
			header.createCell(8).setCellValue("Email Id");
			header.createCell(9).setCellValue("Mobile No");

			int rowNum = 1;
			for (UptimeReportResultDTO dto : data) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(dto.getSrno() != null ? dto.getSrno() : 0);
				row.createCell(1).setCellValue(dto.getUserid() != null ? dto.getUserid() : "");
				row.createCell(2)
						.setCellValue(dto.getTransactionAchieved() != null ? dto.getTransactionAchieved() : 0.0);
				row.createCell(3).setCellValue(dto.getTransactionTarget() != null ? dto.getTransactionTarget() : 0.0);
				row.createCell(4).setCellValue(dto.getUptimeTarget() != null ? dto.getUptimeTarget() : 0.0);
				row.createCell(5).setCellValue(dto.getUptimeAchieved() != null ? dto.getUptimeAchieved() : 0.0);
				row.createCell(6).setCellValue(dto.getAtmCount() != null ? dto.getAtmCount() : 0);
				row.createCell(7).setCellValue(dto.getFullName() != null ? dto.getFullName() : "");
				row.createCell(8).setCellValue(dto.getEmailId() != null ? dto.getEmailId() : "");
				row.createCell(9).setCellValue(dto.getMobileNo() != null ? dto.getMobileNo() : "");

			}

			try (FileOutputStream out = new FileOutputStream(path)) {
				workbook.write(out);
			}
		}
	}

	private void writeCsv(List<UptimeReportResultDTO> data, String path) throws IOException {
		try (FileOutputStream out = new FileOutputStream(path)) {
			StringBuilder sb = new StringBuilder();

			// Header
			sb.append(
					" Sr No, User Id, Transaction Achieved, Transaction Target, Uptime Target, Uptime Achieved, ATM Count, Full Name, Email Id, Mobile No\n");
			// Rows
			for (UptimeReportResultDTO dto : data) {
				sb.append(dto.getSrno() != null ? dto.getSrno() : 0).append(",");
				sb.append(dto.getUserid() != null ? dto.getUserid() : "").append(",");
				sb.append(dto.getTransactionAchieved() != null ? dto.getTransactionAchieved() : 0.0).append(",");
				sb.append(dto.getTransactionTarget() != null ? dto.getTransactionTarget() : 0.0).append(",");
				sb.append(dto.getUptimeTarget() != null ? dto.getUptimeTarget() : 0.0).append(",");
				sb.append(dto.getUptimeAchieved() != null ? dto.getUptimeAchieved() : 0.0).append(",");
				sb.append(dto.getAtmCount() != null ? dto.getAtmCount() : 0).append(",");
				sb.append(dto.getFullName() != null ? dto.getFullName() : "").append(",");
				sb.append(dto.getEmailId() != null ? dto.getEmailId() : "").append(",");
				sb.append(dto.getMobileNo() != null ? dto.getMobileNo() : "").append("\n");
			}
			out.write(sb.toString().getBytes());
		}
	}
}
