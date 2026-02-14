package com.hpy.mappingservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "atm_reassignment_history_atm_wise")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ATMReassignmentHistory_AtmWise {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "atm_code", nullable = false, length = 50)
	private String atmCode;

	@Column(name = "original_ce_user_id", nullable = false, length = 100)
	private String originalCeUserId;

	@Column(name = "new_ce_user_id", nullable = false, length = 100)
	private String newCeUserId;

	@Column(name = "employee_code", length = 50)
	private String employeeCode;

	@Column(name = "home_address", length = 500)
	private String homeAddress;

	@Column(name = "atm_count")
	private Integer atmCount;

	@Column(name = "distance", precision = 10, scale = 3)
	private Double distance;

	@Column(name = "travel_time", precision = 10, scale = 3)
	private Double travelTime;

	@Column(name = "assignment_status", length = 20)
	@Builder.Default
	private String assignmentStatus = "PROPOSED"; // PROPOSED, APPROVED, REJECTED, COMPLETED

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "created_by", length = 100)
	private String createdBy;

	@Column(name = "approved_date")
	private LocalDateTime approvedDate;

	@Column(name = "approved_by", length = 100)
	private String approvedBy;

	@Column(name = "remarks", length = 1000)
	private String remarks;

	@Column(name = "rank_position")
	private Integer rankPosition;

	@Column(name = "is_selected")
	@Builder.Default
	private Boolean isSelected = false;

	@PrePersist
	protected void onCreate() {
		if (createdDate == null) {
			createdDate = LocalDateTime.now();
		}
	}
}
