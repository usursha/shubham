package com.hpy.ops360.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDocumentDto {

	@Exclude
	private String document;
	private String documentName;

	@Exclude
	private String document1;
	private String document1Name;

	@Exclude
	private String document2;
	private String document2Name;

	@Exclude
	private String document3;
	private String document3Name;

	@Exclude
	private String document4;
	private String document4Name;

}
