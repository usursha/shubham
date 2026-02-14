package com.hpy.ops360.dashboard.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmSynopsisResponse extends GenericDto{

	private static final long serialVersionUID = 1L;
	private String currentStatus;
	private String flagStatus;
	
	private String timeLaps;
	private String totalUptime;
//	private String downTime;
	
	private String visitCompletion ;
	private String distanceCoverd;
	
	private String nameOfChannelExecutive;
//	private boolean isActive;
	
}
