package com.hpy.ops360.ticketing.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

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
public class TicketTask {

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "comment")
	private String comment;

	@CreatedBy
	@Column(name = "created_by")
	private String createdBy;

	@CreationTimestamp
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "reason")
	private String reason;

	@Column(name = "status")
	private String status;

	@Column(name = "checker_name")
	private String checkerName;

	@Column(name = "checker_comment")
	private String checkerComment;

	@Column(name = "atm_code")
	private String atmCode;

	@Column(name = "address")
	private String address;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "total_task")
	private long totalTask;

//	
}