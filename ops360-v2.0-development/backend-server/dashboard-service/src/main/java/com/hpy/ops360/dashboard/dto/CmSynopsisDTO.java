package com.hpy.ops360.dashboard.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmSynopsisDTO extends GenericDto {

	private String currentStatus;

	private String timeLaps;

	private Integer flagStatus;

	private String channelExecutiveName;

	private String totalUptime;

	private String downMachines;

	private String visitCompletion;

	private String distanceCovered;

	private Integer countFlagStatus;

	private Integer ticketEx;

	private String trgtColourCode;

	private String sampathiTrgt;
	private String transactionachieved;
	private String target;
	private Double percentage_change;
	
	private String CeUserName;

}
