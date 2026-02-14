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

import com.hpy.ops360.report_service.dto.DownTicketReportDTO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CEUptimeFileExportService {

    
    @Value("${ops360.assets.base-path}")
    private String BASE_PATH;

    @Value("${ops360.assets.base-url}")
    private String BASE_URL;

    public String exportToFile(List<DownTicketReportDTO> data, String format) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String filename = "uptime_" + timestamp + (format.equalsIgnoreCase("excel") ? ".xlsx" : ".csv");
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

    private void writeExcel(List<DownTicketReportDTO> data, String path) throws IOException {
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

            try (FileOutputStream out = new FileOutputStream(path)) {
                workbook.write(out);
            }
        }
    }

    private void writeCsv(List<DownTicketReportDTO> data, String path) throws IOException {
        try (FileOutputStream out = new FileOutputStream(path)) {
            StringBuilder sb = new StringBuilder();

            // Header
            sb.append("ATM ID,Business Category,Site Type,Bank,Status,Ticket Number,Downtime (Hours),Downtime Bucket,Vendor,Owner,Sub Call Type,Customer Remark,CE Full Name,CM Full Name,Created DateTime,CE ETA DateTime,CE Action DateTime\n");

            // Rows
         // Rows
            for (DownTicketReportDTO dto : data) {
                sb.append(dto.getAtmid() != null ? dto.getAtmid() : "").append(",");
                sb.append(dto.getBusinesscategory() != null ? dto.getBusinesscategory() : "").append(",");
                sb.append(dto.getSitetype() != null ? dto.getSitetype() : "").append(",");
                sb.append(dto.getBank() != null ? dto.getBank() : "").append(",");
                sb.append(dto.getStatus() != null ? dto.getStatus() : "").append(",");
                sb.append(dto.getTicketnumber() != null ? dto.getTicketnumber() : "").append(",");
                sb.append(dto.getDowntimeinhrs() != null ? dto.getDowntimeinhrs() : "").append(",");
                sb.append(dto.getDowntimebucket() != null ? dto.getDowntimebucket() : "").append(",");
                sb.append(dto.getVendor() != null ? dto.getVendor() : "").append(",");
                sb.append(dto.getOwner() != null ? dto.getOwner() : "").append(",");
                sb.append(dto.getSubcalltype() != null ? dto.getSubcalltype() : "").append(",");
                sb.append(dto.getCustomerremark() != null ? dto.getCustomerremark().replace(",", " ") : "").append(",");
                sb.append(dto.getCefullname() != null ? dto.getCefullname() : "").append(",");
                sb.append(dto.getCmfullname() != null ? dto.getCmfullname() : "").append(",");
                sb.append(dto.getCreateddatetime() != null ? dto.getCreateddatetime() : "").append(",");
                sb.append(dto.getCeetadatetime() != null ? dto.getCeetadatetime() : "").append(",");
                sb.append(dto.getCeactiondatetime() != null ? dto.getCeactiondatetime() : "").append("\n");
            }
            out.write(sb.toString().getBytes());
        }
    }
}
