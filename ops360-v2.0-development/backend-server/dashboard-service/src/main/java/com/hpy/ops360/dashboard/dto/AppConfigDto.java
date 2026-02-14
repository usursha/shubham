package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AppConfigDto extends GenericDto {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;

	private String version;
	private String apkUrl;
	private int locationFrequency;
	private int apiFrequency;
	private boolean enableLog;
	private boolean enableScreenshot;
	private boolean enableDeveloperMode;
	private int maximumFileSize;
	private int ticketMaximumFileUpload;

}
