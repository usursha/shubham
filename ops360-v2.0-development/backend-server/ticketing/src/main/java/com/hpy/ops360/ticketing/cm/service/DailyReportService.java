package com.hpy.ops360.ticketing.cm.service;


import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.AtmIdUptimeReportDto;
import com.hpy.ops360.ticketing.cm.dto.CEWiseUptimeReportDto;
import com.hpy.ops360.ticketing.cm.dto.ReportDetailsDto;
import com.hpy.ops360.ticketing.cm.entity.AtmIdUptimeReportEntity;
import com.hpy.ops360.ticketing.cm.entity.CEWiseUptimeReportEntity;
import com.hpy.ops360.ticketing.cm.entity.ReportDetailsEntity;
import com.hpy.ops360.ticketing.cm.repo.AtmIdWiseReportRepository;
import com.hpy.ops360.ticketing.cm.repo.CeWiseReportRepository;
import com.hpy.ops360.ticketing.cm.repo.DailyReportRepository;
import com.hpy.ops360.ticketing.util.AtmIdWiseUptimeExcelGenerator;
import com.hpy.ops360.ticketing.util.CEWiseUptimeExcelGenerator;
import com.hpy.ops360.ticketing.util.DailyExcelGenerator;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DailyReportService {
	
//	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
	

    @Autowired
    private DailyExcelGenerator excelGenerator;
    
    @Autowired
    private CEWiseUptimeExcelGenerator ceWiseUptimeExcelGenerator;
    
    @Autowired
    private AtmIdWiseUptimeExcelGenerator atmIdWiseUptimeExcelGenerator;
    
    @Autowired
    private CeWiseReportRepository ceWiseReportRepository;
    
    @Autowired
    private AtmIdWiseReportRepository atmIdWiseReportRepository;
    
    @Autowired
    private DailyReportRepository reportRepository;

    public XSSFWorkbook generateReport(String calltype, Integer isbreakdown, String OpenDate_uid, String status) {
        List<ReportDetailsEntity> reportData = reportRepository.getOpsDailyReportexcData(calltype, isbreakdown, OpenDate_uid, status);
        log.info("**** Inside DailyReportService ****");
        log.info("report data Returned from repository: "+ reportData);
        
        List<ReportDetailsDto> response=reportData.stream().map(result -> new ReportDetailsDto(
        		result.getSr_no(),
    			result.getTicketNumber(), 
    		    result.getBankAsPerSynergy(),
    		    result.getBankAsPerOps360(),
    		    result.getAtmId(),
    		    result.getAtmCategory(),
    		    result.getSiteType(),
    		    result.getAddress(),
    		    result.getCity(),
    		    result.getState(),
    		    result.getZone(),
    		    result.getZonalHead(),
    		    result.getStateHead(),
    		    result.getChannelManager(),
    		    result.getChannelExecutive(),
    		    result.getFieldServiceCoordinator(),
    		    result.getOwner(),
    		    result.getSubCall(),
    		    result.getEventCode(),
    		    result.getVendor(),
    		    result.getDowntimeHoursMin(),
    		    result.getDowntimeBucket(),
    		    result.getTicketCreatedDateTime(),
    		    result.getExpectedTat(),
    		    result.getFirstDispatchDateTime(),
    		    result.getReallocatedDocketNo(),
    		    result.getLastAllocatedBy(),
    		    result.getLastAllocatedDateTime(),
    		    result.getCesActionDateTime(),
    		    result.getCeEtaUpdatedDateTime(),
    		    result.getCallCloseDateTime(),
    		    result.getCallCloseWithinTatOrOutOfTat(),
    		    result.getCustomerRemarks(),
    		    result.getCeInternalRemarks(),
    		    result.getLastActivityDateTime(),
    		    result.getLastCommentAsPerSynergy(),
    		    result.getNextFollowUpDateTime(),
    		    result.getActionTaken(),
    		    result.getTicketStatus())
    			).toList();
    	
        return excelGenerator.generateExcel(response);
    }
    
    
    
    
    
    public XSSFWorkbook generateCEWiseuptimeReport(String rcmUserId, String scmUserId, String cmUserId, String ceUserId, String startDate, String endDate) {
        log.info("**** Inside downloadCeWiseUptimeReport Service ****");
        log.info("Request received: rcmUserId=" + rcmUserId + ", scmUserId=" + scmUserId + ", cmUserId=" + cmUserId + ", ceUserId=" + ceUserId + ", startDate=" + startDate + ", endDate=" + endDate);
    	List<CEWiseUptimeReportEntity> reportData = ceWiseReportRepository.getCeWiseexcData(rcmUserId, scmUserId, cmUserId, ceUserId, startDate, endDate);
        log.info("**** Inside generateCEWiseuptimeReport ****");
        log.info("Report data Returned from repository: "+ reportData);
        
        List<CEWiseUptimeReportDto> response=reportData.stream().map(result -> new CEWiseUptimeReportDto(
//        		result.getSrno(),
        		result.getDate(),                       // Transaction Date
                result.getZone(),                                  // Zone
                
                result.getZonalHead(),                             // Zonal Head
                result.getZonalHeadContactDetails(),               // Zonal Head Contact Details
                result.getZonalHeadEmail(),                        // Zonal Head Email
                
                result.getStateHead(),                             // State Head
                result.getStateHeadContactDetails(),               // State Head Contact Details
                result.getStateHeadEmail(),                        // State Head Email
                
                result.getChannelManager(),                        // Channel Manager
                result.getChannelManagerContactDetails(),          // Channel Manager Contact Details
                result.getChannelManagerEmail(),                   // Channel Manager Email
                
                result.getChannelExecutive(),                      // Channel Executive
                result.getChannelExecutiveContactDetails(),        // Channel Executive Contact Details
                result.getChannelExecutiveEmail(),                 // Channel Executive Email
                
                result.getNoOfAssignedAtm(),                        // Number of Assigned ATMs
                result.getUptimeTarget(),                           // Uptime Target
                result.getMtdUptime(),                              // MTD Uptime
                result.getMtdTxnTarget(),                           // MTD Transaction Target
                result.getMtdTxnAchieved())
    			).toList();
    	
        return ceWiseUptimeExcelGenerator.generateExcel(response);
    }
    
    
    
    
    
    
    
    public XSSFWorkbook generateATMIdWiseuptimeReport(String startDate, String endDate, String atmIds, String status) {
        log.info("**** Inside downloadCeWiseUptimeReport Service ****");
        log.info("Request received: startDate=" + startDate + ", endDate=" + endDate + ", atmIds=" + atmIds + ", status=" + status);
    	List<AtmIdUptimeReportEntity> reportData = atmIdWiseReportRepository.getAtmIdWiseexcData(startDate, endDate, atmIds, status);
        log.info("**** Inside generateCEWiseuptimeReport ****");
        log.info("Report data Returned from repository: "+ reportData);
        
        List<AtmIdUptimeReportDto> response=reportData.stream().map(result -> new AtmIdUptimeReportDto(
        		result.getSrNo(),                                  // Serial Number
        		result.getDate(),                                   // Transaction Date
        		result.getAtmidCurrent(),                           // ATM ID Current
        		result.getBank(),                                   // Bank Name
        		result.getOldAtmIds(),                              // Old ATM IDs
        		result.getPsId(),                                   // PS ID
        		result.getAtmLocation(),                            // ATM Location
        		result.getCity(),                                   // City
        		result.getState(),                                  // State
        		result.getZone(),                                   // Zone
        		result.getSiteType(),                               // Site Type
        		result.getLiveDate(),                               // Live Date

        		// Zonal Head Details
        		result.getZonalHead(),                              // Zonal Head
        		result.getZonalHeadUserId(),                        // Zonal Head User ID
        		result.getZonalHeadMobileNumber(),                  // Zonal Head Mobile Number
        		result.getZonalHeadEmailId(),                       // Zonal Head Email ID

        		// SCM Details
        		result.getScmName(),                                // SCM Name
        		result.getScmUserId(),                              // SCM User ID
        		result.getScmMobileNumber(),                        // SCM Mobile Number
        		result.getScmEmailId(),                             // SCM Email ID

        		// CM Details
        		result.getCmName(),                                 // CM Name
        		result.getCmUserId(),                               // CM User ID
        		result.getCmMobileNumber(),                         // CM Mobile Number
        		result.getCmEmailId(),                              // CM Email ID

        		// CE Details
        		result.getCeName(),                                 // CE Name
        		result.getCeUserId(),                               // CE User ID
        		result.getCeMobileNumber(),                         // CE Mobile Number
        		result.getCeMailId(),                               // CE Mail ID

        		// Performance Metrics
        		result.getTarget(),                                 // Target
        		result.getMtdTill31stOct24(),                       // MTD till 31st October 2024
        		result.getMtdTransactionsAchievedPercent(),         // MTD Transactions Achieved Percentage
        		result.getMtdUptime())                              // MTD Uptime

    			).toList();
    	
        return atmIdWiseUptimeExcelGenerator.generateExcel(response);
    }
}

