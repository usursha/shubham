package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;
import com.hpy.ops360.ticketing.utils.TicketColorDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TaskSummaryOfCeResponseFilterDTO extends GenericDto {

	private static final long serialVersionUID = 1L;
	private List<AtmDetailFilterResponseDto> atmFilters;
	private List<BankFilterResponseDTO> bankFilters;
	private List<SiteNameFilterResponseDTO> siteNameFilters;
	private List<VendorFilterResponseDTO> vendorFilters;
	private List<OwnerFilterResponseDTO> ownerFilters;
	private List<ErrorTypeFilterResponseDTO> errorType;
	private List<DownTime_FilterResponseDTO> downTime;
	@Setter
	private TicketColorDto color;

}
