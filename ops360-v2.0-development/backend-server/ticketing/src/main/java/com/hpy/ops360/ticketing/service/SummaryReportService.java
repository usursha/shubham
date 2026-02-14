package com.hpy.ops360.ticketing.service;

import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.dto.SummaryReportDto;
import com.hpy.ops360.ticketing.repository.SummaryReportRepository;
import com.hpy.ops360.ticketing.util.SummaryExcelGenerator;

@Service
public class SummaryReportService {

	@Autowired
	private SummaryExcelGenerator summaryexcelGenerator;

	@Autowired
	private SummaryReportRepository summaryreportRepository;

	public XSSFWorkbook generateReport(String username, String reportUser, String startDate, String endDate) {
		List<SummaryReportDto> summaryreportData = summaryreportRepository.getReportData(username, reportUser,
				startDate, endDate);
		return summaryexcelGenerator.generateExcel(summaryreportData);
	}

}
