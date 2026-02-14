package com.hpy.ops360.dashboard.dto;
import lombok.Data;

@Data
public class AppConfigRequestDto {
	private String version;
    private String apkUrl;
    private Integer locationFrequency;
    private Integer apiFrequency;
    private Boolean enableLog;
    private Boolean enableScreenshot;
    private Boolean enableDeveloperMode;
    private Integer maximumFileSize;
    private Integer ticketMaximumFileUpload;
    
    
}
