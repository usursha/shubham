package com.hpy.ops360.ticketing.entity;

import java.time.LocalDateTime;

import com.hpy.ops360.framework.entity.UserLocation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Eta extends UserLocation {

	@Column(name = "ticket_number")
	private String ticketNumber;

	@Column(name = "atm_id")
	private String atmid;

	@Column(name = "owner")
	private String owner;

	@Column(name = "customer_remark")
	private String customerRemark;

	@Column(name = "internal_remark")
	private String internalRemark;

	@Column(name = "eta_date_time")
	private LocalDateTime etaDateTime;

	@Column(name = "document_name")
	private String documentName;

	@Column(name = "document")
	private String document;

}
