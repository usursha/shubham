package com.hpy.ops360.dashboard.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AppConfigNewDto extends GenericDto {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;

	private String version;
	private String apkUrl;
	private Integer locationFrequency;
	private Integer apiFrequency;
	private Boolean enableLog;
	private Boolean enableScreenshot;
	private Boolean enableDeveloperMode;
	private Integer maximumFileSize;
	private Integer ticketMaximumFileUpload;
	private LocalDateTime createdDate;

}
