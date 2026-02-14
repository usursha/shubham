package com.hpy.ops360.ticketing.util;

import org.apache.james.mime4j.dom.datetime.DateTime;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hpy.ops360.ticketing.cm.dto.ReportDetailsDto;
import com.hpy.ops360.ticketing.dto.ReportDto;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class DailyExcelGenerator {

    private static final Logger logger = LoggerFactory.getLogger(ExcelGenerator.class);

    @Value("${dailyreport.columns}")
    private String reportColumns;

    public XSSFWorkbook generateExcel(List<ReportDetailsDto> reportData) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Report");
        logger.info("row:{}",reportData);

        Row headerRow = sheet.createRow(0);
        List<String> columns = Arrays.asList(reportColumns.split(","));
        for (int i = 0; i < columns.size(); i++) {
            headerRow.createCell(i).setCellValue(columns.get(i));
        }

        int rowNum = 1;
        for (ReportDetailsDto report : reportData) {
            Row row = sheet.createRow(rowNum++);
            // Set values to cells from TicketDetailsDto
            row.createCell(0).setCellValue(report.ticketNumber); // Ticket Number
            row.createCell(1).setCellValue(report.bankAsPerSynergy); // BANK as per Synergy
            row.createCell(2).setCellValue(report.bankAsPerOps360); // BANK as per Ops360
            row.createCell(3).setCellValue(report.atmId); // ATM ID
            row.createCell(4).setCellValue(report.atmCategory); // ATM Category
            row.createCell(5).setCellValue(report.siteType); // Site Type
            row.createCell(6).setCellValue(report.address); // Address
            row.createCell(7).setCellValue(report.city); // City
            row.createCell(8).setCellValue(report.state); // State
            row.createCell(9).setCellValue(report.zone); // Zone
            row.createCell(10).setCellValue(report.zonalHead); // Zonal Head
            row.createCell(11).setCellValue(report.stateHead); // State Head
            row.createCell(12).setCellValue(report.channelManager); // Channel Manager
            row.createCell(13).setCellValue(report.channelExecutive); // Channel Executive
            row.createCell(14).setCellValue(report.fieldServiceCoordinator); // Field Service Coordinator
            row.createCell(15).setCellValue(report.owner); // Owner
            row.createCell(16).setCellValue(report.subCall); // SubCall
            row.createCell(17).setCellValue(report.eventCode); // Event Code
            row.createCell(18).setCellValue(report.vendor); // Vendor
            row.createCell(19).setCellValue(report.downtimeHoursMin); // Downtime (Hours/Min)
            row.createCell(20).setCellValue(report.downtimeBucket); // Downtime (Bucket)

            // Formatting dates
            row.createCell(21).setCellValue((report.ticketCreatedDateTime)); // Ticket Created Date & Time
            row.createCell(22).setCellValue(report.expectedTat); // Expected TaT
            row.createCell(23).setCellValue((report.firstDispatchDateTime)); // First Dispatch Date & Time
            row.createCell(24).setCellValue(report.reallocatedDocketNo); // Re-allocated Docket No
            row.createCell(25).setCellValue(report.lastAllocatedBy); // Last Allocated By
            row.createCell(26).setCellValue(report.lastAllocatedDateTime); // Last Allocated Date & Time
            row.createCell(27).setCellValue(report.cesActionDateTime); // CE's Action Date & Time
            row.createCell(28).setCellValue(report.ceEtaUpdatedDateTime); // CE ETA Updated Date & Time
            row.createCell(29).setCellValue(report.callCloseDateTime); // Call Close Date & Time
            row.createCell(30).setCellValue(report.callCloseWithinTatOrOutOfTat); // Call Close within TaT/Out of TaT
            row.createCell(31).setCellValue(report.customerRemarks); // Customer Remarks
            row.createCell(32).setCellValue(report.ceInternalRemarks); // CE Internal Remarks
            row.createCell(33).setCellValue(report.lastActivityDateTime); // Last Activity Date & Time
            row.createCell(34).setCellValue(report.lastCommentAsPerSynergy); // Last comment as per Synergy
            row.createCell(35).setCellValue(report.nextFollowUpDateTime); // Next Follow up Date & Time
            row.createCell(36).setCellValue(report.actionTaken); // Action Taken
            row.createCell(37).setCellValue(report.ticketStatus); // Ticket Status (Open/Updated/Time Out/Close)
        
        }

        return workbook;
    }

    private String convertDateToString(DateTime date) {
    	String dateFormat = "yyyy/MM/dd HH:mm:ss";
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    
}

