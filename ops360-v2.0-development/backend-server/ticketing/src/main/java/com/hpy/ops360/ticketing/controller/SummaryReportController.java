package com.hpy.ops360.ticketing.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import com.hpy.ops360.ticketing.service.SummaryReportService;

import lombok.extern.slf4j.Slf4j;

import com.hpy.ops360.ticketing.entity.ReportRequest;
import com.hpy.ops360.ticketing.logapi.Loggable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/summary")
public class SummaryReportController {
//	private static final Logger logger = LoggerFactory.getLogger(SummaryReportController.class);

    @Autowired
    private SummaryReportService summaryreportService;

    @PostMapping("/report")
    @Loggable
    public String generateReport(@RequestBody ReportRequest reportRequest) throws IOException {
    	log.info("Received request to generate Summary report with parameters: {}", reportRequest);
    	
    	try {
        XSSFWorkbook workbook = summaryreportService.generateReport(
                reportRequest.getUsername(),
                reportRequest.getReportUser(),
                reportRequest.getStartDate(),
                reportRequest.getEndDate()
        );
        
        log.info("Generated Summary report successfully for user: {}", reportRequest.getReportUser());

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        String base64Excel = Base64.getEncoder().encodeToString(bos.toByteArray());
        
        log.info("Summary report Excel file sent.");
        return base64Excel;
    	}catch (IOException e) {
    		log.error("Error generating Audit report", e);
    		return "Error Generating Excel File";
    	}
    }
}
