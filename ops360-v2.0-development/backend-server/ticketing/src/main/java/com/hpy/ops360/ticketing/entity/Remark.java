package com.hpy.ops360.ticketing.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.hpy.ops360.framework.entity.UserLocation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "remark")
public class Remark extends UserLocation {

	@Column(name = "remarks", length = 1000)
	private String remarks;

	@Column(name = "remark_by")
	private String remarkBy;

//	@ElementCollection
//	@CollectionTable(name = "remark_attachments", joinColumns = @JoinColumn(name = "remark_id"))
//	@Column(name = "attachment")
//	private List<String> attachments;

//	@Column(name = "parent_remark_id")
//	private Long parentRemarkId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_remark_id", referencedColumnName = "id", updatable = false)
	private Remark parentRemark;

	@Transient
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "parent_remark_id")
	private List<Remark> childRemarks;

	@Column(name = "seq_number")
	private Integer seqNumber;

	@ManyToOne
	@JoinColumn(name = "ticket_id")
	private Ticket ticket; // Relationship with Ticket

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

}
