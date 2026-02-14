package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.ticketing.entity.BroadCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketAtmDetailsDto extends GenericDto{

	@JsonIgnore
	private Long id;

	private String ticketNumber;// srno
	private String problem;
	private String startTime; // createdate
	private String endTime; // completiondatewithtime
	private int expectedTat;
	private String atmNo; // equipmentid
	private String address; // address
	private double mtdUptime; // monthTillDateUptime
	private String cra;
	private List<VendorDetailsDto> vendorDetails;
	private String bankAccount;
	private String Owner;
	private String subcallType;
	private String etaDateTime;
	private String etaTimeout;
	private Integer travelEta;
	private String createDateTime;

}
