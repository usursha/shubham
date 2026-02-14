package com.hpy.ops360.ticketing.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hpy.ops360.framework.entity.UserLocation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "remark_assets")
public class RemarkAssets extends UserLocation {

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "file_name")
	private String fileName;

	@CreatedBy
	@Column(name = "created_by")
	private String createdBy;

	@CreationTimestamp
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Column(updatable = false, nullable = false)
	private LocalDate createdDate;

	@LastModifiedBy
	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@UpdateTimestamp
	@Column(name = "last_modified_date")
	private LocalDateTime lastModifiedDate;

	@Column(name = "remark_id")
	private Long remarkId;

	@Column(name = "parent_remark_id")
	private Long parentRemarkId;

}
