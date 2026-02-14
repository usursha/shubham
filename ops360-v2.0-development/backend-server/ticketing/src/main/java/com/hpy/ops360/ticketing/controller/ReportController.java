package com.hpy.ops360.ticketing.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.entity.ReportRequest;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.service.ReportService;

@RestController
@RequestMapping("/audit")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @PostMapping("/report")
    @Loggable
    public String generateReport(@RequestBody ReportRequest reportRequest) {
        logger.info("Received request to generate Audit report with parameters: {}", reportRequest);

        try {
            XSSFWorkbook workbook = reportService.generateReport(
                    reportRequest.getUsername(),
                    reportRequest.getReportUser(),
                    reportRequest.getStartDate(),
                    reportRequest.getEndDate()
            );

            logger.info("Audit report Generated successfully for user: {}", reportRequest.getReportUser());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            String base64Excel = Base64.getEncoder().encodeToString(bos.toByteArray());            

            logger.info("Audit report Excel file encoded to Base64 and sent in response.");
            return base64Excel;

        } catch (IOException e) {
            logger.error("Error generating Audit report", e);
            return "Error generating report";
        }

      
    }
}
