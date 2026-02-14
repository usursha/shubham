package com.hpy.ops360.atmservice.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketFilterOptionsResponse extends GenericDto{

	@JsonIgnore
	private Long id;
	private List<SortOption> sortOptions;
	private List<TicketTypeOption> ticketTypeOptions;
	private List<CategoryOption> categoryOptions;
	private List<SubCallTypeOption> subCallTypeOptions;
	private List<StatusOption> statusOptions;
	private List<TicketAgingHourOption> ticketAgingHourOptions; // Result Set 6 - Hour-based aging
	private List<TicketAgingDayOption> ticketAgingDayOptions;
	private List<OwnerOption> ownerOptions;
	private List<VendorOption> vendorOptions;
	private List<CreationDateOption> creationDateOptions;
	
}