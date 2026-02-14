package com.hpy.ops360.ticketing.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hpy.ops360.ticketing.dto.ReportDto;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class ExcelGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ExcelGenerator.class);

    @Value("${report.columns}")
    private String reportColumns;

    public XSSFWorkbook generateExcel(List<ReportDto> reportData) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");
        logger.info("row:{}",reportData);

        Row headerRow = sheet.createRow(0);
        List<String> columns = Arrays.asList(reportColumns.split(","));
        for (int i = 0; i < columns.size(); i++) {
            headerRow.createCell(i).setCellValue(columns.get(i));
        }

        int rowNum = 1;
        for (ReportDto report : reportData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(report.getId());
            row.createCell(1).setCellValue(report.getUsername());
            row.createCell(2).setCellValue(convertDateToString(report.getActionTime()));
            row.createCell(3).setCellValue(report.getActionTaken());
            row.createCell(4).setCellValue(report.getAtmId());
            row.createCell(5).setCellValue(report.getTicketNo());
            row.createCell(6).setCellValue(convertDateToString(report.getEtaUpdatedTo()));
            row.createCell(7).setCellValue(report.getTravelMode());
            row.createCell(8).setCellValue(convertDateToString(report.getTravelEta()));
            row.createCell(9).setCellValue(report.getWorkMode());
            row.createCell(10).setCellValue(report.getOwner());
            row.createCell(11).setCellValue(report.getCustRemarks());
            row.createCell(12).setCellValue(report.getInternalRemarks());
            row.createCell(13).setCellValue(report.getCreateTicketReason());
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

