package com.hpy.ops360.ticketing.cm.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmIdUptimeReportDto extends GenericDto{
	
	@JsonIgnore
	private Long id;

    private Long srNo;

    private String date;

    private String atmidCurrent;

    private String bank;

    private String oldAtmIds;

    private String psId;

    private String atmLocation;

    private String city;

    private String state;

    private String zone;

    private String siteType;

    private String liveDate;

    private String zonalHead;

    private String zonalHeadUserId;

    private String zonalHeadMobileNumber;

    private String zonalHeadEmailId;

    private String scmName;

    private String scmUserId;

    private String scmMobileNumber;

    private String scmEmailId;

    private String cmName;

    private String cmUserId;

    private String cmMobileNumber;

    private String cmEmailId;

    private String ceName;

    private String ceUserId;

    private String ceMobileNumber;

    private String ceMailId;

    private String target;

    private String mtdTill31stOct24;

    private Float mtdTransactionsAchievedPercent;

    private String mtdUptime;

	public AtmIdUptimeReportDto(Long srNo, String date, String atmidCurrent, String bank, String oldAtmIds, String psId,
			String atmLocation, String city, String state, String zone, String siteType, String liveDate,
			String zonalHead, String zonalHeadUserId, String zonalHeadMobileNumber, String zonalHeadEmailId,
			String scmName, String scmUserId, String scmMobileNumber, String scmEmailId, String cmName, String cmUserId,
			String cmMobileNumber, String cmEmailId, String ceName, String ceUserId, String ceMobileNumber,
			String ceMailId, String target, String mtdTill31stOct24, Float mtdTransactionsAchievedPercent,
			String mtdUptime) {
		super();
		this.srNo = srNo;
		this.date = date;
		this.atmidCurrent = atmidCurrent;
		this.bank = bank;
		this.oldAtmIds = oldAtmIds;
		this.psId = psId;
		this.atmLocation = atmLocation;
		this.city = city;
		this.state = state;
		this.zone = zone;
		this.siteType = siteType;
		this.liveDate = liveDate;
		this.zonalHead = zonalHead;
		this.zonalHeadUserId = zonalHeadUserId;
		this.zonalHeadMobileNumber = zonalHeadMobileNumber;
		this.zonalHeadEmailId = zonalHeadEmailId;
		this.scmName = scmName;
		this.scmUserId = scmUserId;
		this.scmMobileNumber = scmMobileNumber;
		this.scmEmailId = scmEmailId;
		this.cmName = cmName;
		this.cmUserId = cmUserId;
		this.cmMobileNumber = cmMobileNumber;
		this.cmEmailId = cmEmailId;
		this.ceName = ceName;
		this.ceUserId = ceUserId;
		this.ceMobileNumber = ceMobileNumber;
		this.ceMailId = ceMailId;
		this.target = target;
		this.mtdTill31stOct24 = mtdTill31stOct24;
		this.mtdTransactionsAchievedPercent = mtdTransactionsAchievedPercent;
		this.mtdUptime = mtdUptime;
	}
    
    
}
