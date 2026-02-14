package com.hpy.ops360.ticketing.service;


import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.ReportDto;
import com.hpy.ops360.ticketing.repository.ReportRepository;
import com.hpy.ops360.ticketing.util.ExcelGenerator;

import java.util.List;

@Service
public class ReportService {
	
	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
	

    @Autowired
    private ExcelGenerator excelGenerator;

    @Autowired
    private ReportRepository reportRepository;

    public XSSFWorkbook generateReport(String username, String reportUser, String startDate, String endDate) {
        List<ReportDto> reportData = reportRepository.getReportData(username, reportUser, startDate, endDate);
        return excelGenerator.generateExcel(reportData);
    }
}

