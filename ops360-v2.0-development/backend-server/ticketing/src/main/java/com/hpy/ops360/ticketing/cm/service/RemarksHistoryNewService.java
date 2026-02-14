package com.hpy.ops360.ticketing.cm.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.ticketing.cm.dto.RemarkHistorynewResponseDto;
import com.hpy.ops360.ticketing.cm.dto.RemarksHistoryFinalResponseDto;
import com.hpy.ops360.ticketing.cm.dto.RemarksnewDtoForCm;
import com.hpy.ops360.ticketing.cm.dto.RemarksrequestnewDto;
import com.hpy.ops360.ticketing.cm.entity.RemarkHistoryDetailsNew;
import com.hpy.ops360.ticketing.cm.repo.RemarkHistoryDetailsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RemarksHistoryNewService {

	@Autowired
	private RemarkHistoryDetailsRepository remarkHistoryDetailsRepository;

	 public RemarksHistoryFinalResponseDto getRemarksHistoryCmPortal(RemarksrequestnewDto requestDto) {
	        log.info("RemarksHistoryService | getRemarksHistoryCmPortal:{}", requestDto);
	        
	        
	        try {
	            List<RemarkHistoryDetailsNew> remarkResp = remarkHistoryDetailsRepository
	                .getRemarkHistoryforCm(requestDto.getTicketno(), requestDto.getAtmid(), requestDto.getPageNumber(), requestDto.getPageSize());
	            
	            log.info("Records remarkshistory:{}", remarkResp);
	            int totalCount = 0;
	            if (!remarkResp.isEmpty()) {
	                totalCount = Integer.parseInt(remarkResp.get(0).getTotal_records());
	            }
	            final int finalTotalCount = totalCount;

	            if (!remarkResp.isEmpty()) {
	                List<RemarksnewDtoForCm> remarksList = remarkResp.stream()
	                    .map(record -> new RemarksnewDtoForCm(
	                        null,
	                        Optional.ofNullable(record.getUsername()).orElse(""),
	                        Optional.ofNullable(record.getRemarks()).orElse(""),
	                        Optional.ofNullable(record.getDate()).orElse(""),
	                        Optional.ofNullable(record.getOwner()).orElse(""),
	                        Optional.ofNullable(record.getSubcalltype()).orElse(""),
	                        Optional.ofNullable(record.getEtadatetime()).orElse(""),
	                        Optional.ofNullable(record.getVendor()).orElse("")
	                    ))
	                    .toList();

	                log.info("Retrieved {} remarks for ticket: {}", remarksList.size(), requestDto.getTicketno());
	                List<RemarkHistorynewResponseDto> records = transformRemarks(remarksList);
	                return new RemarksHistoryFinalResponseDto(finalTotalCount, records);
	            }
	            
	            log.info("No remarks found for ticket: {}", requestDto.getTicketno());
	            return new RemarksHistoryFinalResponseDto(0, Collections.emptyList());
	            
	        } catch (Exception e) {
	            log.error("Error fetching remarks: ", e);
	            return new RemarksHistoryFinalResponseDto(0, Collections.emptyList());
	        }
	    }

	    public List<RemarkHistorynewResponseDto> transformRemarks(List<RemarksnewDtoForCm> remarks) {
	        List<SimpleDateFormat> inputFormats = Arrays.asList(
	            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH),
	            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.ENGLISH),
	            new SimpleDateFormat("dd MMM ''yy, hh:mm a", Locale.ENGLISH),
	            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH),
	            new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH),
	            new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.ENGLISH)
	        );

	        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
	        SimpleDateFormat fullDateTimeFormattype = new SimpleDateFormat("dd MMM ''yy, hh:mm a", Locale.ENGLISH);
	        
	        Date today = new Date();
	        String todayString = outputDateFormat.format(today);

	        Map<String, List<RemarksnewDtoForCm>> grouped = remarks.stream().collect(Collectors.groupingBy(remark -> {
	            try {
	                Date remarkDate = parseFlexibleDate(remark.getDate(), inputFormats);
	                String remarkDateString = outputDateFormat.format(remarkDate);
	                return todayString.equals(remarkDateString) ? "Today | " + todayString : remarkDateString;
	            } catch (ParseException e) {
	                throw new RuntimeException("Unparseable date: " + remark.getDate(), e);
	            }
	        }, LinkedHashMap::new, Collectors.toList()));

	        // Format etaTime and date inside each remark
	        grouped.forEach((key, list) -> {
	            for (RemarksnewDtoForCm remark : list) {
	                try {
	                    Date etaDate = parseFlexibleDate(remark.getEtaTime(), inputFormats);
	                    Date remarkDate = parseFlexibleDate(remark.getDate(), inputFormats);
	                    remark.setEtaTime(fullDateTimeFormattype.format(etaDate));
	                    remark.setDate(fullDateTimeFormattype.format(remarkDate));
	                } catch (ParseException e) {
	                    // Handle gracefully or log
	                    remark.setEtaTime("Invalid date");
	                    remark.setDate("Invalid date");
	                }
	            }
	        });

	        return grouped.entrySet().stream()
	            .map(entry -> new RemarkHistorynewResponseDto(entry.getKey(), entry.getValue()))
	            .toList();
	    }
	    
	    private Date parseFlexibleDate(String dateString, List<SimpleDateFormat> formats) throws ParseException {
	        for (SimpleDateFormat format : formats) {
	            try {
	                return format.parse(dateString);
	            } catch (ParseException ignored) {
	                // Try next format
	            }
	        }
	        throw new ParseException("Unable to parse date: " + dateString, 0);
	    }

}



