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

import com.hpy.ops360.report_service.dto.AtmUptimeDTO;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AtmUptimeFileExportService {

    
    @Value("${ops360.assets.base-path}")
    private String BASE_PATH;

    @Value("${ops360.assets.base-url}")
    private String BASE_URL;

    public String exportToFile(List<AtmUptimeDTO> data, String format) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String filename = "atmuptime_" + timestamp + (format.equalsIgnoreCase("excel") ? ".xlsx" : ".csv");
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

    private void writeExcel(List<AtmUptimeDTO> data, String path) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Down Tickets");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ATM ID");
            header.createCell(1).setCellValue("Bank Name");
            header.createCell(2).setCellValue("City");
            header.createCell(3).setCellValue("Address");
            header.createCell(4).setCellValue("Site");
            header.createCell(5).setCellValue("Site Id");
            header.createCell(6).setCellValue("Status");
            header.createCell(7).setCellValue("Installation Date");
            header.createCell(8).setCellValue("Uptime");
            header.createCell(9).setCellValue("CE Full NAME");
            
           
            
            
            int rowNum = 1;
            for (AtmUptimeDTO dto : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dto.getAtmId());
                row.createCell(1).setCellValue(dto.getBankName());
                row.createCell(2).setCellValue(dto.getCity());
                row.createCell(3).setCellValue(dto.getAddress());
                row.createCell(4).setCellValue(dto.getSite());
                row.createCell(5).setCellValue(dto.getSiteId());
                row.createCell(6).setCellValue(dto.getStatus());
                row.createCell(7).setCellValue(dto.getInstallationDate());
                row.createCell(8).setCellValue(dto.getUptime());
                row.createCell(9).setCellValue(dto.getCeFullName());
                

            }

            try (FileOutputStream out = new FileOutputStream(path)) {
                workbook.write(out);
            }
        }
    }

    private void writeCsv(List<AtmUptimeDTO> data, String path) throws IOException {
        try (FileOutputStream out = new FileOutputStream(path)) {
            StringBuilder sb = new StringBuilder();

            // Header
//            sb.append("ATM ID,Business Category,Site Type,Bank,Status,Ticket Number,Downtime (Hours),Downtime Bucket,Vendor,Owner,Sub Call Type,Customer Remark,CE Full Name,CM Full Name,Created DateTime,CE ETA DateTime,CE Action DateTime\n");
            sb.append("ATM ID, Bank Name, City, Address, Site, Site Id, Status, Installation Date, Uptime, CE Full NAME\n");
            // Rows
         // Rows
            for (AtmUptimeDTO dto : data) {
                sb.append(dto.getAtmId() != null ? dto.getAtmId() : "").append(",");
                sb.append(dto.getBankName() != null ? dto.getBankName() : "").append(",");
                sb.append(dto.getCity() != null ? dto.getCity() : "").append(",");
                sb.append(dto.getAddress() != null ? dto.getAddress() : "").append(",");
                sb.append(dto.getSite() != null ? dto.getSite() : "").append(",");
                sb.append(dto.getSiteId() != null ? dto.getSiteId() : "").append(",");
                sb.append(dto.getStatus() != null ? dto.getStatus() : "").append(",");
                sb.append(dto.getInstallationDate() != null ? dto.getInstallationDate() : "").append(",");
                sb.append(dto.getUptime() != null ? dto.getUptime() : "").append(",");
                sb.append(dto.getCeFullName() != null ? dto.getCeFullName() : "").append("\n");
                
            }
            out.write(sb.toString().getBytes());
        }
    }
}
