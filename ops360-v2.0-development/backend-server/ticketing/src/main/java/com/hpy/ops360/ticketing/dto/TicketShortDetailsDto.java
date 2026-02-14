package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketShortDetailsDto extends GenericDto {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8769704231821889548L;
	@JsonIgnore
	private String id;
	private String ceName;
	private String ticketNumber;
	private String atmId;
	private String createdOn;
	private String status; //open
	private String ticketType; //down
	private String downHrs;
	private String owner;
	private String subcalltype;
	private String internalRemarks;
	private String latestEtaDateTime;
	private String vendor;
	
	
	public TicketShortDetailsDto(String ceName, String ticketNumber, String atmId, String createdOn, String status,
			String ticketType, String downHrs, String owner, String subcalltype, String internalRemarks,
			String latestEtaDateTime, String vendor) {
		super();
		this.ceName = ceName;
		this.ticketNumber = ticketNumber;
		this.atmId = atmId;
		this.createdOn = createdOn;
		this.status = status;
		this.ticketType = ticketType;
		this.downHrs = downHrs;
		this.owner = owner;
		this.subcalltype = subcalltype;
		this.internalRemarks = internalRemarks;
		this.latestEtaDateTime = latestEtaDateTime;
		this.vendor = vendor;
	}


	public String getCeName() {
		return ceName;
	}


	public void setCeName(String ceName) {
		this.ceName = ceName;
	}


	public String getTicketNumber() {
		return ticketNumber;
	}


	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}


	public String getAtmId() {
		return atmId;
	}


	public void setAtmId(String atmId) {
		this.atmId = atmId;
	}


	public String getCreatedOn() {
		return createdOn;
	}


	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getTicketType() {
		return ticketType;
	}


	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}


	public String getDownHrs() {
		return downHrs;
	}


	public void setDownHrs(String downHrs) {
		this.downHrs = downHrs;
	}


	public String getOwner() {
		return owner;
	}


	public void setOwner(String owner) {
		this.owner = owner;
	}


	public String getSubcalltype() {
		return subcalltype;
	}


	public void setSubcalltype(String subcalltype) {
		this.subcalltype = subcalltype;
	}


	public String getInternalRemarks() {
		return internalRemarks;
	}


	public void setInternalRemarks(String internalRemarks) {
		this.internalRemarks = internalRemarks;
	}


	public String getLatestEtaDateTime() {
		return latestEtaDateTime;
	}


	public void setLatestEtaDateTime(String latestEtaDateTime) {
		this.latestEtaDateTime = latestEtaDateTime;
	}


	public String getVendor() {
		return vendor;
	}


	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	
	

}
