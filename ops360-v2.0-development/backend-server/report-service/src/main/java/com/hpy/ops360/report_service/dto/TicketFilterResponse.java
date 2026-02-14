package com.hpy.ops360.report_service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketFilterResponse extends GenericDto {
	@JsonIgnore
	private Long id;
	
    private List<AtmidDto> atmid;
    private List<BankDto> bank;
    private List<StatusDto> status;
    private List<TicketNumberDto> ticketnumber;
    private List<OwnerDto> owner;
    private List<SubcallTypeDto> subcalltype;
    private List<CENameDto> CEFullname;
    private List<BusinessModelDto> businessmodel;
    private List<SiteTypeDto> sitetype;
    private List<EtaDateTimeDto> etadatetime;
    private List<String> sortbyfield;

}
