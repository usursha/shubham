package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemarkHistoryDetailsNew {

	@Id
	private Long srno;
	
	@Column(name = "ticketno")
	private String ticketno;

	@Column(name = "atmid")
	private String atmid;

	@Column(name = "date")
	private String date;

	@Column(name = "comments")
	private String comments;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "subcalltype")
	private String subcalltype;

	@Column(name = "username")
	private String username;

	@Column(name = "etadatetime")
	private String etadatetime;

	@Column(name = "owner")
	private String owner;

	@Column(name = "customer_remark")
	private String customerRemark;
	
	@Column(name = "vendor")
	private String vendor;
	
	private String total_records;

	private String current_page;

	private String page_size;

	private String total_pages;
	
	

}