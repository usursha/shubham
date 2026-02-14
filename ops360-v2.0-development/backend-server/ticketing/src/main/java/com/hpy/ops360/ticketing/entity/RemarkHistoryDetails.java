package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemarkHistoryDetails {

	@Id
	private Long srno;
	
	@Column(name = "ticketno", length = 50)
	private String ticketno;

	@Column(name = "atmid", length = 50)
	private String atmid;

	@Column(name = "date")
	private String date;

	@Column(name = "comments", length = 1000)
	private String comments;

	@Column(name = "remarks", length = 1000)
	private String remarks;

	@Column(name = "subcalltype", length = 100)
	private String subcalltype;

	@Column(name = "username", length = 100)
	private String username;

	@Column(name = "etadatetime")
	private String etadatetime;

	@Column(name = "owner", length = 100)
	private String owner;

	@Column(name = "customer_remark", length = 255)
	private String customerRemark;

}