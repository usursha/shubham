package com.hpy.ops360.ticketing.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hpy.ops360.ticketing.cm.dto.TicketHistoryArchiveDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TicketHistoryArchiveExcelGenerator {

	@Value("${ticket.history.archive}")
	private String ticketHistoryArchive;

	@Value("${ops360.assets.base-path}")
	private String BASE_PATH;

	@Value("${ops360.assets.base-url}")
	private String BASE_URL;

	public String exportToexcelURL(List<TicketHistoryArchiveDto> reportData) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
		String filename = "ticket_archive_" + timestamp + (".xlsx");
		String fullPath = BASE_PATH + filename;

		try {
			generateExcelURL(reportData, fullPath);
			// writeXml(data, fullPath);
		} catch (Exception e) {
			throw new RuntimeException("Failed to export file: " + e.getMessage(), e);
		}

		return BASE_URL + filename;
	}

	public String exportToCsvURL(List<TicketHistoryArchiveDto> reportData) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
		String filename = "ticket_archive_" + timestamp + ".csv";
		String fullPath = BASE_PATH + filename;

		try {
			generateCsvFileURL(reportData, fullPath);
		} catch (Exception e) {
			throw new RuntimeException("Failed to export CSV file: " + e.getMessage(), e);
		}

		return BASE_URL + filename;
	}

	public void generateExcelURL(List<TicketHistoryArchiveDto> reportData, String path) throws IOException {

		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Report");
			log.info("row:{}", reportData);
			Row headerRow = sheet.createRow(0);
			List<String> columns = Arrays.asList(ticketHistoryArchive.split(","));

			for (int i = 0; i < columns.size(); i++) {

				headerRow.createCell(i).setCellValue(columns.get(i));
			}

			int rowNum = 1;

			for (TicketHistoryArchiveDto report : reportData) {
				Row row = sheet.createRow(rowNum++);

				row.createCell(0).setCellValue(report.getSrno() != null ? report.getSrno() : "");
				row.createCell(1).setCellValue(report.getCustomer() != null ? report.getCustomer() : "");
				row.createCell(2).setCellValue(report.getEquipmentId() != null ? report.getEquipmentId() : "");
				row.createCell(3).setCellValue(report.getModel() != null ? report.getModel() : "");
				row.createCell(4).setCellValue(report.getAtmCategory() != null ? report.getAtmCategory() : "");
				row.createCell(5)
						.setCellValue(report.getAtmClassification() != null ? report.getAtmClassification() : "");
				row.createCell(6).setCellValue(report.getCallDate() != null ? report.getCallDate() : "");
				row.createCell(7).setCellValue(report.getCreatedDate() != null ? report.getCreatedDate() : "");
				row.createCell(8).setCellValue(report.getCallType() != null ? report.getCallType() : "");
				row.createCell(9).setCellValue(report.getSubCallType() != null ? report.getSubCallType() : "");
				row.createCell(10).setCellValue(
						report.getCompletionDateWithTime() != null ? report.getCompletionDateWithTime() : "");
				row.createCell(11).setCellValue(report.getDowntimeInMins() != null ? report.getDowntimeInMins() : 0); // Handle
																														// null
				row.createCell(12).setCellValue(report.getVendor() != null ? report.getVendor() : "");
				row.createCell(13).setCellValue(report.getServiceCode() != null ? report.getServiceCode() : "");
				row.createCell(14).setCellValue(report.getDiagnosis() != null ? report.getDiagnosis() : "");
				row.createCell(15).setCellValue(report.getEventCode() != null ? report.getEventCode() : "");
				row.createCell(16).setCellValue(report.getHelpdeskName() != null ? report.getHelpdeskName() : "");
				row.createCell(17)
						.setCellValue(report.getLastAllocatedTime() != null ? report.getLastAllocatedTime() : "");
				row.createCell(18).setCellValue(report.getLastComment() != null ? report.getLastComment() : "");
				row.createCell(19).setCellValue(report.getLastActivity() != null ? report.getLastActivity() : "");
				row.createCell(20).setCellValue(report.getStatus() != null ? report.getStatus() : "");
				row.createCell(21).setCellValue(report.getSubStatus() != null ? report.getSubStatus() : "");
				row.createCell(22).setCellValue(report.getRo() != null ? report.getRo() : "");
				row.createCell(23).setCellValue(report.getSite() != null ? report.getSite() : "");
				row.createCell(24).setCellValue(report.getAddress() != null ? report.getAddress() : "");
				row.createCell(25).setCellValue(report.getCity() != null ? report.getCity() : "");
				row.createCell(26).setCellValue(report.getLocationName() != null ? report.getLocationName() : "");
				row.createCell(27).setCellValue(report.getState() != null ? report.getState() : "");
				row.createCell(28).setCellValue(report.getNextFollowUp() != null ? report.getNextFollowUp() : "");
				row.createCell(29).setCellValue(report.getEtaDateTime() != null ? report.getEtaDateTime() : "");
				row.createCell(30).setCellValue(report.getOwner() != null ? report.getOwner() : "");
				row.createCell(31).setCellValue(report.getCustomerRemark() != null ? report.getCustomerRemark() : "");
				// row.createCell(32).setCellValue(report.getInsertDate() != null ?
				// report.getInsertDate() : "");
			}

			try (FileOutputStream out = new FileOutputStream(path)) {
				workbook.write(out);
			}
			log.info("Excel file generated at: {}", path);
		}
	}

	public XSSFWorkbook generateExcel(List<TicketHistoryArchiveDto> reportData) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Report");
		log.info("row:{}", reportData);
		Row headerRow = sheet.createRow(0);
		List<String> columns = Arrays.asList(ticketHistoryArchive.split(","));

		for (int i = 0; i < columns.size(); i++) {

			headerRow.createCell(i).setCellValue(columns.get(i));
		}

		int rowNum = 1;

		for (TicketHistoryArchiveDto report : reportData) {
			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(report.getSrno() != null ? report.getSrno() : "");
			row.createCell(1).setCellValue(report.getCustomer() != null ? report.getCustomer() : "");
			row.createCell(2).setCellValue(report.getEquipmentId() != null ? report.getEquipmentId() : "");
			row.createCell(3).setCellValue(report.getModel() != null ? report.getModel() : "");
			row.createCell(4).setCellValue(report.getAtmCategory() != null ? report.getAtmCategory() : "");
			row.createCell(5).setCellValue(report.getAtmClassification() != null ? report.getAtmClassification() : "");
			row.createCell(6).setCellValue(report.getCallDate() != null ? report.getCallDate() : "");
			row.createCell(7).setCellValue(report.getCreatedDate() != null ? report.getCreatedDate() : "");
			row.createCell(8).setCellValue(report.getCallType() != null ? report.getCallType() : "");
			row.createCell(9).setCellValue(report.getSubCallType() != null ? report.getSubCallType() : "");
			row.createCell(10)
					.setCellValue(report.getCompletionDateWithTime() != null ? report.getCompletionDateWithTime() : "");
			row.createCell(11).setCellValue(report.getDowntimeInMins() != null ? report.getDowntimeInMins() : 0); // Handle
																													// null
			row.createCell(12).setCellValue(report.getVendor() != null ? report.getVendor() : "");
			row.createCell(13).setCellValue(report.getServiceCode() != null ? report.getServiceCode() : "");
			row.createCell(14).setCellValue(report.getDiagnosis() != null ? report.getDiagnosis() : "");
			row.createCell(15).setCellValue(report.getEventCode() != null ? report.getEventCode() : "");
			row.createCell(16).setCellValue(report.getHelpdeskName() != null ? report.getHelpdeskName() : "");
			row.createCell(17).setCellValue(report.getLastAllocatedTime() != null ? report.getLastAllocatedTime() : "");
			row.createCell(18).setCellValue(report.getLastComment() != null ? report.getLastComment() : "");
			row.createCell(19).setCellValue(report.getLastActivity() != null ? report.getLastActivity() : "");
			row.createCell(20).setCellValue(report.getStatus() != null ? report.getStatus() : "");
			row.createCell(21).setCellValue(report.getSubStatus() != null ? report.getSubStatus() : "");
			row.createCell(22).setCellValue(report.getRo() != null ? report.getRo() : "");
			row.createCell(23).setCellValue(report.getSite() != null ? report.getSite() : "");
			row.createCell(24).setCellValue(report.getAddress() != null ? report.getAddress() : "");
			row.createCell(25).setCellValue(report.getCity() != null ? report.getCity() : "");
			row.createCell(26).setCellValue(report.getLocationName() != null ? report.getLocationName() : "");
			row.createCell(27).setCellValue(report.getState() != null ? report.getState() : "");
			row.createCell(28).setCellValue(report.getNextFollowUp() != null ? report.getNextFollowUp() : "");
			row.createCell(29).setCellValue(report.getEtaDateTime() != null ? report.getEtaDateTime() : "");
			row.createCell(30).setCellValue(report.getOwner() != null ? report.getOwner() : "");
			row.createCell(31).setCellValue(report.getCustomerRemark() != null ? report.getCustomerRemark() : "");
			// row.createCell(32).setCellValue(report.getInsertDate() != null ?
			// report.getInsertDate() : "");
		}

		return workbook;

	}

	public void generateCsvFileURL(List<TicketHistoryArchiveDto> reportData, String path) throws IOException {
		if (reportData == null || reportData.isEmpty()) {
			log.error("No data found for CSV generation");
			throw new IOException("No data found for CSV generation");
		}

		String[] headers = Arrays.stream(ticketHistoryArchive.split(",")).map(String::trim).toArray(String[]::new);
		CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(headers).setQuoteMode(QuoteMode.MINIMAL).build();

		try (Writer writer = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8);
				CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {

			for (TicketHistoryArchiveDto report : reportData) {
				csvPrinter.printRecord(report.getSrno() != null ? report.getSrno() : "",
						report.getCustomer() != null ? report.getCustomer() : "",
						report.getEquipmentId() != null ? report.getEquipmentId() : "",
						report.getModel() != null ? report.getModel() : "",
						report.getAtmCategory() != null ? report.getAtmCategory() : "",
						report.getAtmClassification() != null ? report.getAtmClassification() : "",
						report.getCallDate() != null ? report.getCallDate() : "",
						report.getCreatedDate() != null ? report.getCreatedDate() : "",
						report.getCallType() != null ? report.getCallType() : "",
						report.getSubCallType() != null ? report.getSubCallType() : "",
						report.getCompletionDateWithTime() != null ? report.getCompletionDateWithTime() : "",
						report.getDowntimeInMins() != null ? report.getDowntimeInMins() : 0,
						report.getVendor() != null ? report.getVendor() : "",
						report.getServiceCode() != null ? report.getServiceCode() : "",
						report.getDiagnosis() != null ? report.getDiagnosis() : "",
						report.getEventCode() != null ? report.getEventCode() : "",
						report.getHelpdeskName() != null ? report.getHelpdeskName() : "",
						report.getLastAllocatedTime() != null ? report.getLastAllocatedTime() : "",
						report.getLastComment() != null ? report.getLastComment() : "",
						report.getLastActivity() != null ? report.getLastActivity() : "",
						report.getStatus() != null ? report.getStatus() : "",
						report.getSubStatus() != null ? report.getSubStatus() : "",
						report.getRo() != null ? report.getRo() : "", report.getSite() != null ? report.getSite() : "",
						report.getAddress() != null ? report.getAddress() : "",
						report.getCity() != null ? report.getCity() : "",
						report.getLocationName() != null ? report.getLocationName() : "",
						report.getState() != null ? report.getState() : "",
						report.getNextFollowUp() != null ? report.getNextFollowUp() : "",
						report.getEtaDateTime() != null ? report.getEtaDateTime() : "",
						report.getOwner() != null ? report.getOwner() : "",
						report.getCustomerRemark() != null ? report.getCustomerRemark() : ""
				// report.getInsertDate() != null ? report.getInsertDate() : ""
				);
			}

			csvPrinter.flush();
			log.info("CSV file generated at: {}", path);
		}
	}

	public String generateCSV(List<TicketHistoryArchiveDto> reportData) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		// Split and trim headers
		String[] headers = Arrays.stream(ticketHistoryArchive.split(",")).map(String::trim).toArray(String[]::new);

		CSVFormat format = CSVFormat.DEFAULT.builder().setHeader(headers).setQuoteMode(QuoteMode.MINIMAL).build();

		try (Writer writer = new OutputStreamWriter(byteArrayOutputStream, StandardCharsets.UTF_8);

				CSVPrinter csvPrinter = new CSVPrinter(writer, format)) {

			if (reportData == null || reportData.isEmpty()) {
				log.error("No data found for CSV generation");
				throw new IOException("No data found for CSV generation");
			}

			for (TicketHistoryArchiveDto report : reportData) {
				csvPrinter.printRecord(report.getSrno() != null ? report.getSrno() : "",
						report.getCustomer() != null ? report.getCustomer() : "",
						report.getEquipmentId() != null ? report.getEquipmentId() : "",
						report.getModel() != null ? report.getModel() : "",
						report.getAtmCategory() != null ? report.getAtmCategory() : "",
						report.getAtmClassification() != null ? report.getAtmClassification() : "",
						report.getCallDate() != null ? report.getCallDate() : "",
						report.getCreatedDate() != null ? report.getCreatedDate() : "",
						report.getCallType() != null ? report.getCallType() : "",
						report.getSubCallType() != null ? report.getSubCallType() : "",
						report.getCompletionDateWithTime() != null ? report.getCompletionDateWithTime() : "",
						report.getDowntimeInMins() != null ? report.getDowntimeInMins() : 0, // Assuming 0 is a
																								// reasonable default
																								// for an Integer
						report.getVendor() != null ? report.getVendor() : "",
						report.getServiceCode() != null ? report.getServiceCode() : "",
						report.getDiagnosis() != null ? report.getDiagnosis() : "",
						report.getEventCode() != null ? report.getEventCode() : "",
						report.getHelpdeskName() != null ? report.getHelpdeskName() : "",
						report.getLastAllocatedTime() != null ? report.getLastAllocatedTime() : "",
						report.getLastComment() != null ? report.getLastComment() : "",
						report.getLastActivity() != null ? report.getLastActivity() : "",
						report.getStatus() != null ? report.getStatus() : "",
						report.getSubStatus() != null ? report.getSubStatus() : "",
						report.getRo() != null ? report.getRo() : "", report.getSite() != null ? report.getSite() : "",
						report.getAddress() != null ? report.getAddress() : "",
						report.getCity() != null ? report.getCity() : "",
						report.getLocationName() != null ? report.getLocationName() : "",
						report.getState() != null ? report.getState() : "",
						report.getNextFollowUp() != null ? report.getNextFollowUp() : "",
						report.getEtaDateTime() != null ? report.getEtaDateTime() : "",
						report.getOwner() != null ? report.getOwner() : "",
						report.getCustomerRemark() != null ? report.getCustomerRemark() : ""
				// report.getInsertDate() != null ? report.getInsertDate() : ""
				);
			}

			csvPrinter.flush();
		} catch (IOException ex) {
			log.error("Error while generating CSV for Ticket History data", ex);
			throw ex; // Propagate the exception
		}

		return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
	}

}
