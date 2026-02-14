package com.hpy.ops360.ticketing.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.cm.dto.AtmIdUptimeReportDto;
import com.hpy.ops360.ticketing.cm.dto.AtmIdWiseUptimeviewRequestDto;
import com.hpy.ops360.ticketing.cm.dto.AtmidWiseUptimeRequestDto;
import com.hpy.ops360.ticketing.cm.dto.CEWiseUptimeReportDto;
import com.hpy.ops360.ticketing.cm.dto.CEWiseUptimeRequestDto;
import com.hpy.ops360.ticketing.cm.dto.CEWiseUptimeviewRequestDto;
import com.hpy.ops360.ticketing.cm.dto.DailyReportRequest;
import com.hpy.ops360.ticketing.cm.dto.DropdownOptionsDTO;
import com.hpy.ops360.ticketing.cm.dto.ExcelReportRequestDto;
import com.hpy.ops360.ticketing.cm.dto.ReportDetailsDto;
import com.hpy.ops360.ticketing.cm.service.DailyReportService;
import com.hpy.ops360.ticketing.cm.service.DailyReportServiceJson;
import com.hpy.ops360.ticketing.cm.service.DropdownService;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/Report")
public class DailyReportController {

    @Autowired
    private DailyReportService reportService;
    
    @Autowired
    private DropdownService dropdownService;
    
    @Autowired
    private DailyReportServiceJson jsonreportService;
    
    @Autowired
	private  RestUtils restUtils;
    
    @GetMapping("/list/calltype")
    @Loggable
    public ResponseEntity<IResponseDto> getDropdownOptions() {
    	
    	DropdownOptionsDTO response=dropdownService.getDropdownOptions();
        return ResponseEntity.ok(restUtils.wrapResponse(response, "List fetched"));
    }

    @PostMapping("/download/actionable-report")
    @Loggable
    public IResponseDto generateReport(@RequestBody ExcelReportRequestDto reportRequest) throws IOException {
        log.info("Received request to generate Audit report with parameters: {}", reportRequest);

        log.info("**** Inside DailyReport Controller ****");
            XSSFWorkbook workbook = reportService.generateReport(
                    reportRequest.getCalltype(),
                    reportRequest.getIsbreakdown(),
                    reportRequest.getOpenDate_uid(),
                    reportRequest.getStatus()
            );

            log.info("daily report Generated successfully");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            String base64Excel = Base64.getEncoder().encodeToString(bos.toByteArray());            

            log.info("Daily report Excel file encoded to Base64 and sent in response.");
            return restUtils.wrapResponse(base64Excel, "Report Generated Successfully");

        } 
    
    @PostMapping("/view/actionable-report")
    @Loggable
    public ResponseEntity<IResponseDto> generateReportJson(@RequestBody DailyReportRequest reportRequest) {
        log.info("Received request to generate Daily report with parameters: {}", reportRequest);

        log.info("**** Inside DailyReport Controller ****");
        List<ReportDetailsDto> response = jsonreportService.generateReport(
                    reportRequest.getCalltype(),
                    reportRequest.getIsbreakdown(),
                    reportRequest.getOpenDate_uid(),
                    reportRequest.getStatus(),
                    reportRequest.getPageindex(),
                    reportRequest.getDataperpage()
            );
        log.info("Daily report in JSON generated successfully");

        return ResponseEntity.ok(restUtils.wrapResponse(response, "Report Obtained in JSON form"));
    }
    
    @PostMapping("/download/cewiseptime_report")
    @Loggable
    public IResponseDto downloadCeWiseUptimeReport(@RequestBody CEWiseUptimeRequestDto request) throws IOException {
        log.info("Received request to generate CE wise Uptime report with parameters: {}", request);

        log.info("request recieved:- "+ request);
        log.info("**** Inside downloadCeWiseUptimeReport Controller ****");
            XSSFWorkbook workbook = reportService.generateCEWiseuptimeReport(
                    request.getRcmUserId(),
                    request.getScmUserId(),
                    request.getCmUserId(),
                    request.getCeUserId(),
                    request.getStartDate(),
                    request.getEndDate()
            );

            log.info("Ce wise uptime daily report Generated successfully");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            String base64Excel = Base64.getEncoder().encodeToString(bos.toByteArray());            

            log.info("Daily report Excel file encoded to Base64 and sent in response.");
            return restUtils.wrapResponse(base64Excel, "Report Generated Successfully");

        } 
    
    
    
    
    @PostMapping("/view/cewiseptime_report")
    @Loggable
    public IResponseDto viewCeWiseUptimeReport(@RequestBody CEWiseUptimeviewRequestDto request) throws IOException {
        log.info("Received request to generate CE wise Uptime report with parameters: {}", request);

        log.info("request recieved:- "+ request);
        log.info("**** Inside downloadCeWiseUptimeReport Controller ****");
        List<CEWiseUptimeReportDto> response = jsonreportService.viewCEWiseuptimeReport(
                    request.getRcmUserId(),
                    request.getScmUserId(),
                    request.getCmUserId(),
                    request.getCeUserId(),
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getPage(),
                    request.getPageSize()
            );

            log.info("Ce wise uptime report Generated successfully in json form:- "+ response);

            return restUtils.wrapResponse(response, "view Report successfully");

        }
    
    
    
    
    @PostMapping("/download/atmid_wiseuptime_report")
    @Loggable
    public IResponseDto downloadAtmidWiseUptimeReport(@RequestBody AtmidWiseUptimeRequestDto request) throws IOException {
        log.info("Received request to generate CE wise Uptime report with parameters: {}", request);

        log.info("request recieved:- "+ request);
        log.info("**** Inside downloadCeWiseUptimeReport Controller ****");
            XSSFWorkbook workbook = reportService.generateATMIdWiseuptimeReport(
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getAtmIds(),
                    request.getStatus()
            );

            log.info("Ce wise uptime daily report Generated successfully");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            workbook.close();

            String base64Excel = Base64.getEncoder().encodeToString(bos.toByteArray());            

            log.info("Daily report Excel file encoded to Base64 and sent in response.");
            return restUtils.wrapResponse(base64Excel, "Report Generated Successfully");

        }
    
    
    @PostMapping("/view/atmid_wiseuptime_report")
    @Loggable
    public IResponseDto viewAtmidWiseUptimeReport(@RequestBody AtmIdWiseUptimeviewRequestDto request) throws IOException {
        log.info("Received request to generate CE wise Uptime report with parameters: {}", request);

        log.info("request recieved:- "+ request);
        log.info("**** Inside downloadCeWiseUptimeReport Controller ****");
        List<AtmIdUptimeReportDto> response = jsonreportService.viewATMIdWiseuptimeReport(
                    
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getAtmIds(),
                    request.getStatus(),
                    request.getPage(),
                    request.getPagesize()
            );

            log.info("Ce wise uptime report Generated successfully in json form:- "+ response);

            return restUtils.wrapResponse(response, "view Report successfully");

        }
    
}