package com.hpy.ops360.atmservice.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.atmservice.dto.AtmUpDownStatusAndDownTimeDto;
import com.hpy.ops360.atmservice.dto.LoginService;
import com.hpy.ops360.atmservice.entity.AtmTicketEvent;
import com.hpy.ops360.atmservice.entity.TicketDetailsHimsEntity;
import com.hpy.ops360.atmservice.repository.AtmDetailsRepoHims;
import com.hpy.ops360.atmservice.response.TicketDetailsDtoHims;
import com.hpy.ops360.atmservice.utils.CustomDateFormattor;
import com.hpy.ops360.atmservice.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AtmUpDownStatusAndDownTimeService {

    @Autowired
    private SynergyService synergyService;

    @Autowired
    private UserAtmDetailsService userAtmDetailsService;

    @Autowired
    private LoginService loginService;
    
    @Autowired
	private AtmDetailsRepoHims atmDetailsRepoHims;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public AtmUpDownStatusAndDownTimeDto fetchAtmUpdownStatus(String atmId) {

        AtmUpDownStatusAndDownTimeDto result = new AtmUpDownStatusAndDownTimeDto();
           
    //    List<AtmIdDto> atmIds = List.of(new AtmIdDto(atmId));
        List<TicketDetailsHimsEntity> openTicketDetails = atmDetailsRepoHims.findByEquipmentIdNative(atmId);
        log.info("openTicketDetails:{}", openTicketDetails);

        if (openTicketDetails == null || openTicketDetails.isEmpty()) {
        	result.setStatus("Operational"); 
            result.setDownTime("00h 00m");
            return result;
        }

        List<TicketDetailsDtoHims> allTicketDetails = new ArrayList<>();
        StringBuilder atmTicketEventCode = new StringBuilder();
        
        Map<String, TicketDetailsDtoHims> ticketDetailsMap = new HashMap<>();
        for (TicketDetailsHimsEntity atmdetails : openTicketDetails) {
            if (atmdetails != null) {
            	
            	  TicketDetailsDtoHims ticketDto = new TicketDetailsDtoHims(atmdetails.getSrNo() != null ? atmdetails.getSrNo() : "",
                          atmdetails.getCustomer() != null ? atmdetails.getCustomer() : "",
                                  atmdetails.getEquipmentId() != null ? atmdetails.getEquipmentId() : "",
                                  atmdetails.getModel() != null ? atmdetails.getModel() : "",
                                  atmdetails.getAtmCategory() != null ? atmdetails.getAtmCategory() : "",
                                  atmdetails.getAtmClassification() != null ? atmdetails.getAtmClassification() : "",
                                  DateUtil.formatFullTimestamp(atmdetails.getCallDate()),
                                  CustomDateFormattor.convertDate(atmdetails.getCreatedDate(), CustomDateFormattor.FormatStyle.SYNERGY),
                                  atmdetails.getCallType() != null ? atmdetails.getCallType() : "",
                                  atmdetails.getSubCallType() != null ? atmdetails.getSubCallType() : "",
                                  CustomDateFormattor.convert(atmdetails.getCompletionDateWithTime(), CustomDateFormattor.FormatStyle.SYNERGY),
                                  atmdetails.getDowntimeInMins(), 
                                  atmdetails.getVendor() != null ? atmdetails.getVendor() : "",
                                  atmdetails.getServiceCode() != null ? atmdetails.getServiceCode() : "",
                                  atmdetails.getDiagnosis() != null ? atmdetails.getDiagnosis() : "",
                                  atmdetails.getEventCode() != null ? atmdetails.getEventCode() : "",
                                  atmdetails.getHelpdeskName() != null ? atmdetails.getHelpdeskName() : "",
                                  CustomDateFormattor.convertDate(atmdetails.getLastAllocatedTime(), CustomDateFormattor.FormatStyle.SYNERGY),
                                  atmdetails.getLastComment() != null ? atmdetails.getLastComment() : "",
                                  CustomDateFormattor.convert(atmdetails.getLastActivity(), CustomDateFormattor.FormatStyle.SYNERGY), 
                                  atmdetails.getStatus() != null ? atmdetails.getStatus() : "",
                                  atmdetails.getSubStatus() != null ? atmdetails.getSubStatus() : "",
                                  atmdetails.getRo() != null ? atmdetails.getRo() : "",
                                  atmdetails.getSite() != null ? atmdetails.getSite() : "",
                                  atmdetails.getAddress() != null ? atmdetails.getAddress() : "",
                                  atmdetails.getCity() != null ? atmdetails.getCity() : "",
                                  atmdetails.getLocationName() != null ? atmdetails.getLocationName() : "",
                                  atmdetails.getState() != null ? atmdetails.getState() : "",
                                  CustomDateFormattor.convertDate(atmdetails.getNextFollowUp(), CustomDateFormattor.FormatStyle.SYNERGY),
                                  CustomDateFormattor.convertDate(atmdetails.getEtaDateTime(), CustomDateFormattor.FormatStyle.SYNERGY),
                                  atmdetails.getOwner() != null ? atmdetails.getOwner() : "",
                                  atmdetails.getCustomerRemark() != null ? atmdetails.getCustomerRemark() : ""
                              );
            	
                allTicketDetails.add(ticketDto);

                for (TicketDetailsDtoHims ticketDetailsDto : allTicketDetails) {
                    String uniqueCode = String.format("%s|%s|%s,",
                            ticketDetailsDto.getEquipmentid(),
                            ticketDetailsDto.getSrno(),
                            ticketDetailsDto.getEventcode());
                    atmTicketEventCode.append(uniqueCode);
                    ticketDetailsMap.put(ticketDetailsDto.getEquipmentid() + ticketDetailsDto.getSrno(), ticketDetailsDto);
                }
            }
        }
        log.info("Aggregated allTicketDetails:{}", allTicketDetails);

        String atmTicketEventCodeList = "";
        if (!atmTicketEventCode.isEmpty()) { 
            atmTicketEventCodeList = atmTicketEventCode.substring(0, atmTicketEventCode.length() - 1);
        }
        log.info("atmTicketEventCode after trim:{}", atmTicketEventCodeList);

        List<AtmTicketEvent> atmTicketEventList = userAtmDetailsService
                .getAtmTicketEvent(loginService.getLoggedInUser(), atmTicketEventCodeList);
        log.info("atmTicketEventList:{}", atmTicketEventList);

        long breakdownTicketCount = atmTicketEventList.stream()
                .filter(event -> event.getIsBreakdown() == 1)
                .count(); 

        log.info("isBreakDownCount:{}", breakdownTicketCount);

        // Determine status and calculate downtime based on breakdown tickets
        if (!allTicketDetails.isEmpty() && breakdownTicketCount > 0) {
            result.setStatus("Down");

            Optional<TicketDetailsDtoHims> firstTicket = allTicketDetails.stream()
	                .min(Comparator.comparing(ticket -> LocalDateTime.parse(ticket.getCreateddate(), DATE_TIME_FORMATTER)));

            if (firstTicket.isPresent()) {
                String firstCreatedDateStr = firstTicket.get().getCreateddate();
                log.info("First Created Date (Ascending Order): {}", firstCreatedDateStr);

                try {
                    LocalDateTime startDateTime = LocalDateTime.parse(firstCreatedDateStr, DATE_TIME_FORMATTER);
                    LocalDateTime currentDateTime = LocalDateTime.now(); // Get current date and time

                    Duration duration = Duration.between(startDateTime, currentDateTime);

                    long totalMinutes = duration.toMinutes();
                    long hours = totalMinutes / 60;
                    long minutes = totalMinutes % 60;
                    String downtimeString = String.format("%02dh %02dm", hours, minutes);
                    
                    result.setDownTime(downtimeString);
                    log.info("Calculated Downtime (only hours and minutes): {}", downtimeString);

                } catch (DateTimeParseException e) {
                    log.error("Final error parsing date for downtime calculation '{}': {}", firstCreatedDateStr, e.getMessage());
                    result.setDownTime("Error calculating downtime");
                }
            } else {
                log.info("No valid ticket details found to determine the first created date for downtime calculation.");
                result.setDownTime("N/A");
            }
        }else { 
            result.setStatus("Operational");
            result.setDownTime("00h 00m");
        }

        return result;
    }
    
    private String formatDateField(Date date) {
	    return date != null ? DateUtil.formatDateStringIndexOfATM(date.toString()) : "";
	}
}