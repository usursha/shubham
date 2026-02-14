package com.hpy.ops360.ticketing.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hpy.ops360.ticketing.cm.dto.AtmIdUptimeReportDto;
import com.hpy.ops360.ticketing.cm.dto.CEWiseUptimeReportDto;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AtmIdWiseUptimeExcelGenerator {


    @Value("${atmidwisedailyreport.columns}")
    private String reportColumns;

    public XSSFWorkbook generateExcel(List<AtmIdUptimeReportDto> reportData) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("AtmId Wise Uptime Report");
        log.info("Generating AtmId wise Uptime Report Excel with {} records", reportData.size());

        Row headerRow = sheet.createRow(0);
        List<String> columns = Arrays.asList(reportColumns.split(","));
        for (int i = 0; i < columns.size(); i++) {
            headerRow.createCell(i).setCellValue(columns.get(i));
        }

        int rowNum = 1;
        for (AtmIdUptimeReportDto report : reportData) {
            Row row = sheet.createRow(rowNum++);
            
         // Basic Information
//            row.createCell(0).setCellValue(report.getSrNo()); // Serial Number
            row.createCell(0).setCellValue(report.getDate()); // Transaction Date
            row.createCell(1).setCellValue(report.getAtmidCurrent()); // ATM ID Current
            row.createCell(2).setCellValue(report.getBank()); // Bank Name
            row.createCell(3).setCellValue(report.getOldAtmIds()); // Old ATM IDs
            row.createCell(4).setCellValue(report.getPsId()); // PS ID
            row.createCell(5).setCellValue(report.getAtmLocation()); // ATM Location
            row.createCell(6).setCellValue(report.getCity()); // City
            row.createCell(7).setCellValue(report.getState()); // State
            row.createCell(8).setCellValue(report.getZone()); // Zone
            row.createCell(9).setCellValue(report.getSiteType()); // Site Type
            row.createCell(10).setCellValue(report.getLiveDate()); // Live Date

            // Zonal Head Information
            row.createCell(11).setCellValue(report.getZonalHead()); // Zonal Head
            row.createCell(12).setCellValue(report.getZonalHeadUserId()); // Zonal Head User ID
            row.createCell(13).setCellValue(report.getZonalHeadMobileNumber()); // Zonal Head Mobile Number
            row.createCell(14).setCellValue(report.getZonalHeadEmailId()); // Zonal Head Email ID

            // SCM Information
            row.createCell(15).setCellValue(report.getScmName()); // SCM Name
            row.createCell(16).setCellValue(report.getScmUserId()); // SCM User ID
            row.createCell(17).setCellValue(report.getScmMobileNumber()); // SCM Mobile Number
            row.createCell(18).setCellValue(report.getScmEmailId()); // SCM Email ID

            // CM Information
            row.createCell(19).setCellValue(report.getCmName()); // CM Name
            row.createCell(20).setCellValue(report.getCmUserId()); // CM User ID
            row.createCell(21).setCellValue(report.getCmMobileNumber()); // CM Mobile Number
            row.createCell(22).setCellValue(report.getCmEmailId()); // CM Email ID

            // CE Information
            row.createCell(23).setCellValue(report.getCeName()); // CE Name
            row.createCell(24).setCellValue(report.getCeUserId()); // CE User ID
            row.createCell(25).setCellValue(report.getCeMobileNumber()); // CE Mobile Number
            row.createCell(26).setCellValue(report.getCeMailId()); // CE Mail ID

            // Performance Metrics
            row.createCell(27).setCellValue(report.getTarget()); // Target
            row.createCell(28).setCellValue(report.getMtdTill31stOct24()); // MTD till 31st October 2024
            row.createCell(29).setCellValue(report.getMtdTransactionsAchievedPercent()); // MTD Transactions Achieved Percentage
            row.createCell(30).setCellValue(report.getMtdUptime()); // MTD Uptime

        }

        return workbook;
    }
}
