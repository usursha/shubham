package com.hpy.ops360.ticketing.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetManuallyTicketResponseDTO {

	private String atmId;
    private String comment;
    private String createdBy;
    private LocalDateTime createdDate;
    private String diagnosis;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
    private String reason;
    private String refNo;
    private String status;
    private String ticketNumber;
    private String username;
    private String checkerName;
    private LocalDateTime checkerTime;
    private String checkerRejectReason;
    private String checkerComment;
    private String crmStatus;
    private LocalDateTime crmTime;
}
