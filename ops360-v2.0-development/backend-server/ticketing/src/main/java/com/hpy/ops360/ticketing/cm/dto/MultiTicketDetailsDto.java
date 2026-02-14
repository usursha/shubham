package com.hpy.ops360.ticketing.cm.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultiTicketDetailsDto extends GenericDto {

	@JsonIgnore
	private Long id;

	private String srno;
	private String createdDate;
	private String owner;
	private String vendor;
	private String subcallType;
	private String uploadedBy;
	private int isClosed;
	private int isDown;
	private int isOpen;
	private String hoursPassed;
	private int imageCount;
	private List<String> imageUrl;

}
