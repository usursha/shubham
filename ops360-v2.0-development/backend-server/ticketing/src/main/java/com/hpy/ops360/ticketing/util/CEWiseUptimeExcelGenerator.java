package com.hpy.ops360.ticketing.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hpy.ops360.ticketing.cm.dto.CEWiseUptimeReportDto;

import java.util.Arrays;
import java.util.List;

@Component
public class CEWiseUptimeExcelGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CEWiseUptimeExcelGenerator.class);

    @Value("${cewisedailyreport.columns}")
    private String reportColumns;

    public XSSFWorkbook generateExcel(List<CEWiseUptimeReportDto> reportData) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("CEWise Uptime Report");
        logger.info("Generating CEWise Uptime Report Excel with {} records", reportData.size());

        Row headerRow = sheet.createRow(0);
        List<String> columns = Arrays.asList(reportColumns.split(","));
        for (int i = 0; i < columns.size(); i++) {
            headerRow.createCell(i).setCellValue(columns.get(i));
        }

        int rowNum = 1;
        for (CEWiseUptimeReportDto report : reportData) {
            Row row = sheet.createRow(rowNum++);
            
            // Fill the cells with data
            row.createCell(0).setCellValue(report.getDate()); // Transaction Date
            row.createCell(1).setCellValue(report.getZone()); // Zone
            
            // Zonal Head Information
            row.createCell(2).setCellValue(report.getZonalHead()); // Zonal Head
            row.createCell(3).setCellValue(report.getZonalHeadContactDetails()); // Zonal Head Contact Details
            row.createCell(4).setCellValue(report.getZonalHeadEmail()); // Zonal Head Email

            // State Head Information
            row.createCell(5).setCellValue(report.getStateHead()); // State Head
            row.createCell(6).setCellValue(report.getStateHeadContactDetails()); // State Head Contact Details
            row.createCell(7).setCellValue(report.getStateHeadEmail()); // State Head Email

            // Channel Manager Information
            row.createCell(8).setCellValue(report.getChannelManager()); // Channel Manager
            row.createCell(9).setCellValue(report.getChannelManagerContactDetails()); // Channel Manager Contact Details
            row.createCell(10).setCellValue(report.getChannelManagerEmail()); // Channel Manager Email

            // Channel Executive Information
            row.createCell(11).setCellValue(report.getChannelExecutive()); // Channel Executive
            row.createCell(12).setCellValue(report.getChannelExecutiveContactDetails()); // Channel Executive Contact Details
            row.createCell(13).setCellValue(report.getChannelExecutiveEmail()); // Channel Executive Email

            // ATM and Performance Metrics
            row.createCell(14).setCellValue(report.getNoOfAssignedAtm()); // Number of Assigned ATMs
            row.createCell(15).setCellValue(report.getUptimeTarget()); // Uptime Target
            row.createCell(16).setCellValue(report.getMtdUptime()); // MTD Uptime
            row.createCell(17).setCellValue(report.getMtdTxnTarget()); // MTD Transaction Target
            row.createCell(18).setCellValue(report.getMtdTxnAchieved()); // MTD Transactions Achieved
        }

        return workbook;
    }
}
