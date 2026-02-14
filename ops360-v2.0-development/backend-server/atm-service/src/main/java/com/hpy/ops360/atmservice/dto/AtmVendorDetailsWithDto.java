package com.hpy.ops360.atmservice.dto;

import java.io.Serializable;
import java.util.List;

import com.hpy.ops360.atmservice.vendor.dto.VendorDetailsDto;

import lombok.Data;

@Data
public class AtmVendorDetailsWithDto implements Serializable {

	private static final long serialVersionUID = 7235268861941198125L;
	private String atmId;
	private String address;
	private double mtdUptime;
	private String cra;
	private List<VendorDetailsDto> vendorDetails;
	private List<TicketDto> ticketHistory;

}
