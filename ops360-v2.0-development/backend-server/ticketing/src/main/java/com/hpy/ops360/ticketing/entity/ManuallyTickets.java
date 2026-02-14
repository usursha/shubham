package com.hpy.ops360.ticketing.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class ManuallyTickets {

	@Id
	private Long sr_no;
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
