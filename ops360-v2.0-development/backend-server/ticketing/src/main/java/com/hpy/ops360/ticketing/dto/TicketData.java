package com.hpy.ops360.ticketing.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.hpy.ops360.ticketing.utils.TicketColorDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketData {
    private String requestId;
    private String atmId;
    private String ticketNumber;
    private String bank;
    private String siteName;
    private String owner;
    private String subcall;
    private String vendor;
    private String error;
    private String downTime;
    private Double priorityScore;
    private String eventGroup;
    private Integer isBreakdown;
    private Integer isUpdated;
    private Integer isTimedOut;
    private Integer isTravelling;
    private Date travelTime;
    private Integer travelEta;
    private Integer downCall;
    private String etaDateTime;
    private String etaTimeout;
    private String createdDate;
    private String closeDate;
    private Integer flagStatus;
    private LocalDateTime flagStatusInsertTime;
    private TicketColorDto color;
    private String ceName;
}