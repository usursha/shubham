package com.hpy.ops360.dashboard.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class OpenTicketsRequest_cm {

	private String requestid;
	private List<CEAtmDetailsDtoFor_Cm> atmlist;
}
