package com.hpy.ops360.ticketing.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hpy.ops360.ticketing.dto.SummaryReportDto;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class SummaryExcelGenerator {


    @Value("${summary.columns}")
    private String reportColumns;

    public XSSFWorkbook generateExcel(List<SummaryReportDto> summaryreportData) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Summary Report");
        
        log.info("row:{}", summaryreportData);

        // Create header row
        Row headerRow = sheet.createRow(0);
        List<String> columns = Arrays.asList(reportColumns.split(","));
        for (int i = 0; i < columns.size(); i++) {
            headerRow.createCell(i).setCellValue(columns.get(i));
        }

        int rowNum = 1;
        for (SummaryReportDto report : summaryreportData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(report.getId());
            row.createCell(1).setCellValue(report.getAtmId());
            row.createCell(2).setCellValue(report.getTicketId());
            row.createCell(3).setCellValue(report.getEventCode());
            row.createCell(4).setCellValue(report.getCeName());
            row.createCell(5).setCellValue(convertDateToString(report.getFirstFetchTime()));
            row.createCell(6).setCellValue(convertDateToString(report.getLastFetchTime()));
            row.createCell(7).setCellValue(report.getTicketFetchCount());
            row.createCell(8).setCellValue(report.getCeUpdateCount());
        }


        return workbook;
    }

    private String convertDateToString(Date date) {
    	String dateFormat = "yyyy/MM/dd HH:mm:ss";
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    
}
