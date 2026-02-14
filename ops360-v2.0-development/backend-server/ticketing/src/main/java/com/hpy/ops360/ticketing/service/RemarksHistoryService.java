package com.hpy.ops360.ticketing.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

import com.hpy.ops360.ticketing.config.DisableSslClass;
import com.hpy.ops360.ticketing.dto.FollowUp;
import com.hpy.ops360.ticketing.dto.RemarkHistoryResponseDto;
import com.hpy.ops360.ticketing.dto.RemarksDto;
import com.hpy.ops360.ticketing.dto.RemarksDtoForCm;
import com.hpy.ops360.ticketing.dto.RemarksrequestDto;
import com.hpy.ops360.ticketing.entity.OwnerBySubcall;
import com.hpy.ops360.ticketing.entity.RemarkHistoryDetails;
import com.hpy.ops360.ticketing.entity.RemarksHistory;
import com.hpy.ops360.ticketing.login.service.LoginService;
import com.hpy.ops360.ticketing.repository.FollowupHistoryDetailsRepository;
import com.hpy.ops360.ticketing.repository.OwnerBySubcallRepository;
import com.hpy.ops360.ticketing.repository.ParentChildTicketNoRepository;
import com.hpy.ops360.ticketing.repository.RemarksHistoryRepository;
import com.hpy.ops360.ticketing.ticket.dto.RemarksResponseDto;
import com.hpy.ops360.ticketing.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RemarksHistoryService {

//private static final Logger log = LoggerFactory.getLogger(EtaService.class);
    
    @Autowired
    private ParentChildTicketNoRepository parentChildTicketNoRepository;
    
    @Autowired
    private FollowupHistoryDetailsRepository followupHistoryDetailsRepository;
    
    @Autowired
    private RemarksHistoryRepository remarksHistoryRepository;
    
    @Autowired
    private SynergyService synergyService;
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
	private OwnerBySubcallRepository ownerBySubcallRepository;
    

    
    /**
     * Determine ticket source by checking both open and closed ticket views
     */
    private String getTicketSource(String ticketId, String atmId) {
        try {
            // First check open tickets
            String source = parentChildTicketNoRepository.getTicketSource(atmId);
            if (source != null && !source.isEmpty()) {
                return source;
            }
            
            // If not found in open tickets, check closed tickets
            source = parentChildTicketNoRepository.getClosedTicketSource(ticketId, atmId);
            if (source != null && !source.isEmpty()) {
                return source;
            }
            
            log.warn("No source found for ticket: {} and ATM: {}", ticketId, atmId);
            return "Synergy"; // Default to Synergy if no source found
            
        } catch (Exception e) {
            log.error("Error getting ticket source: ", e);
            return "Synergy"; // Default to Synergy on error
        }
    }
    
    /**
     * Get remarks history from HIMS (GetFollowup_History_Details table)
     */
    private List<RemarksDto> getRemarksHistoryFromHims(RemarksrequestDto requestDto) {
        log.info("remarksService | getRemarksHistoryFromHims:{}", requestDto);
        
        try {
            List<RemarkHistoryDetails> himsRecords = followupHistoryDetailsRepository
                .getFollowupHistoryByTicketAndAtm(requestDto.getTicketno(), requestDto.getAtmid());
            
            log.info("himsRecords remarkshistory:{}",himsRecords);
            
            if (!himsRecords.isEmpty()) {
                List<RemarksDto> remarksList = himsRecords.stream()
                    .map(record -> new RemarksDto(
                        record.getUsername(),
                        record.getRemarks(), // CE comments
                        record.getDate(),
                        record.getOwner(),
                        record.getSubcalltype()
                    ))
                    .collect(Collectors.toList());
                
                log.info("Retrieved {} remarks from HIMS for ticket: {}", remarksList.size(), requestDto.getTicketno());
                return remarksList;
            }
            
            log.info("No remarks found in HIMS for ticket: {}", requestDto.getTicketno());
            return Collections.emptyList();
            
        } catch (Exception e) {
            log.error("Error fetching remarks from HIMS: ", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * Get remarks history from  (stored procedure)
     */
    private List<RemarksDto> getRemarksHistoryFromSp(RemarksrequestDto requestDto) {
        log.info("EtaService | getRemarksHistoryFromSynergy:{}", requestDto);
        
        try {
            List<RemarksHistory>Records = remarksHistoryRepository
                .getRemarksHistory(loginService.getLoggedInUser(), requestDto.getTicketno(), requestDto.getAtmid());
            
            if (!Records.isEmpty()) {
                List<RemarksDto> remarksList = Records.stream()
                    .map(record -> new RemarksDto(
                        record.getCreatedBy(),
                        record.getInternalRemark(),
                        record.getCreatedDate(),
                        record.getOwner(),
                        record.getSubcall()
                    ))
                    .collect(Collectors.toList());
                
                log.info("Retrieved {} remarks from Synergy for ticket: {}", remarksList.size(), requestDto.getTicketno());
                return remarksList;
            }
            
            log.info("No remarks found in Synergy for ticket: {}", requestDto.getTicketno());
            return Collections.emptyList();
            
        } catch (Exception e) {
            log.error("Error fetching remarks from Synergy: ", e);
            return Collections.emptyList();
        }
    }
    
    private void setOwnerBYSubcallType(List<RemarksDto> remarks) {
		for (RemarksDto remark : remarks) {
			LocalDateTime date = LocalDateTime.parse(remark.getDate(), DateUtil.outputFormatter);
			log.info("Comment from synergy or hims -----------:{}", remark.getComment().toString());
			log.info("--------------", date);
			Optional<OwnerBySubcall> ownerBySubcall = ownerBySubcallRepository
					.getOwnerBySubcallType(remark.getSubcallType());
			if (ownerBySubcall.isPresent()) {
				log.info("owner from sp:{}", ownerBySubcall.get().getOwner());
				remark.setOwner(ownerBySubcall.get().getOwner());
			}
		}
	}
//    public List<RemarksDto> getRemarksHistoryFromSp(RemarksrequestDto requestsdto) {
//		log.info("EtaService | getRemarksHistoryFromSp:{}", requestsdto);
//		List<RemarksHistory> remarksHistories = remarksHistoryRepository
//				.getRemarksHistory(loginService.getLoggedInUser(), requestsdto.getTicketno(), requestsdto.getAtmid());
//
//		if (!remarksHistories.isEmpty()) {
//			return remarksHistories.stream().map(remarks -> new RemarksDto(remarks.getCreatedBy(),
//					remarks.getInternalRemark(), remarks.getCreatedDate(), remarks.getOwner(), remarks.getSubcall()))
//					.toList();
//
//		}
//		return Collections.emptyList();
//	}

    
    /**
     * UPDATED: Modified to check source and fetch from appropriate source
     * This replaces the old getMergedRemarksHistory method
     */
    public List<RemarksDto> getMergedRemarksHistory(RemarksrequestDto requestDto) {
        DisableSslClass.disableSSLVerification();
        log.info("1) getMergedRemarksHistory|requestDto:{}", requestDto);
        
        
            // Determine the source of the ticket
            String ticketSource = getTicketSource(requestDto.getTicketno(), requestDto.getAtmid());
            log.info("2) Ticket source determined: {} for ticket:------------------------------------- {}", ticketSource, requestDto.getTicketno());
            
            List<RemarksDto> remarksFromSource;
            
            if ("hims".equalsIgnoreCase(ticketSource)) {
                log.info("3) ---------Fetching remarks from HIMS for ticket: {}", requestDto.getTicketno());
                remarksFromSource = getRemarksHistoryFromHims(requestDto);
                remarksFromSource.forEach(remark -> remark.setDate(DateUtil.formatDateStringCehims(remark.getDate())));
            } else {
                log.info("3-1)-----------Fetching remarks from Synergy for ticket: {}", requestDto.getTicketno());
                remarksFromSource = getRemarksHistory(requestDto);
                remarksFromSource.forEach(remark -> remark.setDate(DateUtil.formatDateStringCe(remark.getDate())));

            }
            
            if (remarksFromSource.isEmpty()) {
                return Collections.emptyList();
            }
            
            // Format dates using DateUtil
           // remarksFromSource.forEach(remark -> remark.setDate(DateUtil.formatDateStringCehims(remark.getDate())));
            log.info("remarks from {} date filtered:{}", ticketSource, remarksFromSource);
            
            setOwnerBYSubcallType(remarksFromSource);
            
            // Sort by date descending (newest first)
            remarksFromSource.sort((r1, r2) -> LocalDateTime.parse(r2.getDate(), DateUtil.outputFormatter)
                    .compareTo(LocalDateTime.parse(r1.getDate(), DateUtil.outputFormatter)));
            
            return remarksFromSource;
            
        
    }
    
    
   
    public List<RemarksDto> getRemarksHistory(RemarksrequestDto requestsdto) {
        log.info("EtaService | getRemarksHistory:{}", requestsdto);
        RemarksResponseDto remarks = synergyService.getRemarksHistory(requestsdto);
        List<RemarksDto> remarkList = new ArrayList<>();
        if (remarks.getFollowUp() != null && !remarks.getFollowUp().isEmpty()) {
            for (FollowUp followUp : remarks.getFollowUp()) {
                if (followUp == null) {
                    continue;
                }
                remarkList.add(new RemarksDto(followUp.getUsername(), followUp.getComments(), followUp.getDate(), "",
                        followUp.getSubcalltype()));
            }
            return remarkList;
        }
        log.info("EtaService | getRemarksHistory from synergy:{}", remarkList);
        return Collections.emptyList();
    }
    
    public List<RemarkHistoryResponseDto> getRemarksHistoryFromHimsPortal(RemarksrequestDto requestDto) {
        log.info("remarksService | getRemarksHistoryFromHims:{}", requestDto);
        
        try {
            List<RemarkHistoryDetails> himsRecords = followupHistoryDetailsRepository
                .getFollowupHistoryWithOwnerByTicketAndAtmCm(requestDto.getTicketno(), requestDto.getAtmid());
            
            log.info("himsRecords remarkshistory:{}",himsRecords);
            
            if (!himsRecords.isEmpty()) {
                List<RemarksDtoForCm> remarksList = himsRecords.stream()
                    .map(record -> new RemarksDtoForCm(
                        null, 
                        record.getUsername(),
                        record.getRemarks(), // CE comments
                        record.getDate(),
                        record.getOwner(),
                        record.getSubcalltype(),
                        record.getEtadatetime() // Assuming etaTime is part of the record
                    ))
                    .toList();
                
                
                
                
                log.info("Retrieved {} remarks from HIMS for ticket: {}", remarksList.size(), requestDto.getTicketno());
                return transformRemarks(remarksList);
            }
            
            log.info("No remarks found in HIMS for ticket: {}", requestDto.getTicketno());
            return Collections.emptyList();
            
        } catch (Exception e) {
            log.error("Error fetching remarks from HIMS: ", e);
            return Collections.emptyList();
        }
    }
    
    public List<RemarkHistoryResponseDto> transformRemarks(List<RemarksDtoForCm> remarks) {
        List<SimpleDateFormat> inputFormats = Arrays.asList(
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.ENGLISH),
            new SimpleDateFormat("dd MMM ''yy, hh:mm a", Locale.ENGLISH),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH),
            new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)
        );

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd MMM ''yy", Locale.ENGLISH);
        
        SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("dd MMM ''yy, hh:mm a", Locale.ENGLISH);


        Date today = new Date();
        String todayString = outputDateFormat.format(today);

        Map<String, List<RemarksDtoForCm>> grouped = remarks.stream().collect(Collectors.groupingBy(remark -> {
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
            for (RemarksDtoForCm remark : list) {
                try {
                    Date etaDate = parseFlexibleDate(remark.getEtaTime(), inputFormats);
                    Date remarkDate = parseFlexibleDate(remark.getDate(), inputFormats);
                    remark.setEtaTime(fullDateTimeFormat.format(etaDate));
                    remark.setDate(fullDateTimeFormat.format(remarkDate));
                } catch (ParseException e) {
                    // Handle gracefully or log
                    remark.setEtaTime("Invalid date");
                    remark.setDate("Invalid date");
                }
            }
        });

        return grouped.entrySet().stream()
                .map(entry -> new RemarkHistoryResponseDto(entry.getKey(), entry.getValue()))
                .toList();
    }


    // Helper method to try multiple formats
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
