package com.hpy.ops360.ticketing.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.hpy.ops360.framework.entity.UserLocation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket extends UserLocation {

	@Column(name = "ticket_number")
	private String ticketNumber;

	@Column(name = "atm_id")
	private String atmId;

	@Column(name = "requested_by")
	private String requestedBy;

	@Column(name = "reason")
	private String reason;

	@Column(name = "status")
	private String status;

	@Column(name = "comment")
	private String comment;

	@CreatedBy
	@Column(name = "created_by")
	private String createdBy;

	@CreationTimestamp
	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@LastModifiedBy
	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@UpdateTimestamp
	@Column(name = "last_modified_date")
	private LocalDateTime lastModifiedDate;

	// One-to-many relationship with Remark
//	@OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
//	private List<Remark> remarks;

}
