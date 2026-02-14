package com.hpy.ops360.ticketing.dto;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.Data;

@Data
public class TicketAssetsDto extends UserLocationDto {

	private static final long serialVersionUID = 1L;

	private Long remarkId;

	private String filePath;

	private String fileName;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private String lastModifiedDate;

}
