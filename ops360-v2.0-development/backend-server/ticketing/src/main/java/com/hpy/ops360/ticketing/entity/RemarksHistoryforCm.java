package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity
public class RemarksHistoryforCm {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "user")
	private String createdBy;

	@Column(name = "owner")
	private String owner;

	@Column(name = "subcall_type")
	private String subcall;

	@Column(name = "internal_remark")
	private String internalRemark;

	@Column(name = "created_date")
	private String createdDate;
	
	@Column(name = "eta_time")
	private String etaTime; 

}
