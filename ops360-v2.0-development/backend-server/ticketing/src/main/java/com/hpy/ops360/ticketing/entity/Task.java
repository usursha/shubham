package com.hpy.ops360.ticketing.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.hpy.ops360.framework.entity.UserLocation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task extends UserLocation {

	@Id
	private Long id;

	@Column(name = "ticket_number")
	private String ticketNumber;

	@Column(name = "atm_id")
	private String atmid;

	@Column(name = "comment")
	private String comment;

	@CreatedBy
	@Column(name = "created_by")
	private String createdBy;

	@CreationTimestamp
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "diagnosis")
	private String diagnosis;

	@Column(name = "document")
	private String document;

	@Column(name = "document_name")
	private String documentName;

	@LastModifiedBy
	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@UpdateTimestamp
	@Column(name = "last_modified_date")
	private LocalDateTime lastModifiedDate;

	@Column(name = "reason")
	private String reason;

	@Column(name = "ref_no")
	private String refNo; // not sure what it is

	@Column(name = "status")
	private String status;

	@Column(name = "username")
	private String username;

	@Column(name = "checker_name")
	private String checkerName;

	@Column(name = "checker_time")
	private LocalDateTime checkerTime;

	@Column(name = "checker_reject_reason")
	private String checkerRejectReason;

	@Column(name = "checker_comment")
	private String checkerComment;

	@Column(name = "crm_status")
	private String crmStatus;

	@Column(name = "crm_time")
	private LocalDateTime crmTime;

}
