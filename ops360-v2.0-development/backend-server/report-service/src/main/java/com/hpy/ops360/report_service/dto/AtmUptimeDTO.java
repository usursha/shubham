package com.hpy.ops360.report_service.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtmUptimeDTO extends GenericDto {
	
	@JsonIgnore
	private Long id;
    private String atmId;
    private String bankName;
    private String city;
    private String address;
    private String site;
    private String siteId;
    private String status;
    private String installationDate;
    private Double uptime;	
    private String ceFullName;
    

}
