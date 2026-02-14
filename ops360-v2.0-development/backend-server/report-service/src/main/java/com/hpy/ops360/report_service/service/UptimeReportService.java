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
import com.hpy.ops360.report_service.dto.DowtimeDataDTO;
import com.hpy.ops360.report_service.dto.UptimeReportRequest;
import com.hpy.ops360.report_service.dto.UptimeReportRequestDownload;
import com.hpy.ops360.report_service.dto.UptimeReportRequestSearch;
import com.hpy.ops360.report_service.dto.UptimeReportResultDTO;
import com.hpy.ops360.report_service.dto.UptimeReportdownloadRequest;
import com.hpy.ops360.report_service.entity.UptimeReportResult;
import com.hpy.ops360.report_service.entity.UptimeReportResultDownload;
import com.hpy.ops360.report_service.repository.UptimeReportDownloadRepository;
import com.hpy.ops360.report_service.repository.UptimeReportRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UptimeReportService {

    @Autowired
    private UptimeReportRepository uptimeReportRepository;

    @Autowired
    private UptimeReportDownloadRepository uptimeReportdownloadRepository;

    @Autowired
    private Helper helper;

    public DowtimeDataDTO getReport(UptimeReportRequest request) {
        log.info("Fetching uptime report for user: {}, from {} to {}, searchKey: {}",
                helper.getLoggedInUser(), request.getStartdate(), request.getEnddate(), request.getSearchkey());

        DowtimeDataDTO response = new DowtimeDataDTO();
        List<UptimeReportResult> data = uptimeReportRepository.getUptimeReport(
                helper.getLoggedInUser(),
                request.getStartdate(),
                request.getEnddate(),
                request.getSearchkey(),
                request.getUptimeAchievedRange(),
                request.getUptimeTargetRange(),
                request.getTxnAchievedRange(),
                request.getTxnTargetRange(),
                request.getPageNumber(),
                request.getPageSize());

        log.debug("Fetched {} records from repository", data.size());

        if (data.isEmpty()) {
            response.setTotalCount(0);
            log.warn("No data found for the given parameters.");
        } else {
            response.setTotalCount(data.get(0).getTotalCount());
            log.info("Total count set to {}", response.getTotalCount());
        }

        List<UptimeReportResultDTO> result = data.stream()
                .map(record -> new UptimeReportResultDTO(
                        record.getSrno(),
                        record.getUserid(),
                        record.getTransactionAchieved(),
                        record.getTransactionTarget(),
                        record.getUptimeTarget(),
                        record.getUptimeAchieved(),
                        record.getAtmCount(),
                        record.getFullName(),
                        record.getEmailId(),
                        record.getMobileNo()))
                .toList();

        response.setData(result);
        log.info("Returning {} DTO records", result.size());
        return response;
    }

    public List<String> getFilteredUserIds(UptimeReportRequestSearch request) {
        log.info("Filtering user IDs by full name containing: {}", request.getSearchkey());

        List<UptimeReportResult> data = uptimeReportRepository.getUptimeReport(
                helper.getLoggedInUser(),
                request.getStartdate(),
                request.getEnddate(),
                request.getSearchkey(),
                request.getUptimeAchievedRange(),
                request.getUptimeTargetRange(),
                request.getTxnAchievedRange(),
                request.getTxnTargetRange(),
                null,
                null);

        List<String> filteredUserIds = data.stream()
                .map(UptimeReportResult::getFullName)
                .filter(fullName -> fullName != null && fullName.toLowerCase().contains(request.getSearchkey().toLowerCase()))
                .collect(Collectors.toList());

        log.info("Filtered {} user IDs matching search key", filteredUserIds.size());
        return filteredUserIds;
    }

    public List<UptimeReportResultDTO> getDownloadReport(UptimeReportRequestDownload request) {
        log.info("Downloading uptime report for user: {}, from {} to {}",
                helper.getLoggedInUser(), request.getStartdate(), request.getEnddate());

        List<UptimeReportResultDownload> data = uptimeReportdownloadRepository.getUptimeReportDownload(
                helper.getLoggedInUser(),
                request.getStartdate(),
                request.getEnddate());

        log.debug("Fetched {} records for download", data.size());

        List<UptimeReportResultDTO> result = data.stream()
                .map(record -> new UptimeReportResultDTO(
                        record.getSrno(),
                        record.getUserid(),
                        record.getTransactionAchieved(),
                        record.getTransactionTarget(),
                        record.getUptimeTarget(),
                        record.getUptimeAchieved(),
                        record.getAtmCount(),
                        record.getFullName(),
                        record.getEmailId(),
                        record.getMobileNo()))
                .toList();

        log.info("Returning {} DTO records for download", result.size());
        return result;
    }
    
    
    
    public List<UptimeReportResultDTO> getReportdownload(UptimeReportdownloadRequest request) {
        log.info("Fetching uptime report for user: {}, from {} to {}, searchKey: {}",
                helper.getLoggedInUser(), request.getStartdate(), request.getEnddate(), request.getSearchkey());

        List<UptimeReportResult> data = uptimeReportRepository.getUptimeReport(
                helper.getLoggedInUser(),
                request.getStartdate(),
                request.getEnddate(),
                request.getSearchkey(),
                request.getUptimeAchievedRange(),
                request.getUptimeTargetRange(),
                request.getTxnAchievedRange(),
                request.getTxnTargetRange(),
                null,
                null);

        log.debug("Fetched {} records from repository", data.size());

        

        List<UptimeReportResultDTO> result = data.stream()
                .map(record -> new UptimeReportResultDTO(
                        record.getSrno(),
                        record.getUserid(),
                        record.getTransactionAchieved(),
                        record.getTransactionTarget(),
                        record.getUptimeTarget(),
                        record.getUptimeAchieved(),
                        record.getAtmCount(),
                        record.getFullName(),
                        record.getEmailId(),
                        record.getMobileNo()))
                .toList();

        log.info("Returning {} DTO records", result.size());
        return result;
    }
    
    
    
    public String writeExcel(List<UptimeReportResultDTO> data) throws IOException {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("CEUptime Tickets");

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

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);

			workbook.close();

			String base64Excel = Base64.getEncoder().encodeToString(bos.toByteArray());
			return base64Excel;
		}
	}
}
