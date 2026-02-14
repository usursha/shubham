package com.hpy.ops360.dashboard.entity;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "App_Config")
@Data
public class AppConfigEntity {

	@Id
    private final int id = 1; // Constant primary key with a fixed value of 1

    @Column(name = "version")
    private String version;

    @Column(name = "apk_url")
    private String apkUrl;

    @Column(name = "location_frequency")
    private int locationFrequency;

    @Column(name = "api_frequency")
    private int apiFrequency;

    @Column(name = "enable_log")
    private boolean enableLog;

    @Column(name = "enable_screenshot")
    private boolean enableScreenshot;

    @Column(name = "enable_developer_mode")
    private boolean enableDeveloperMode;

    @Column(name = "maximum_file_size")
    private int maximumFileSize;

    @Column(name = "ticket_maximum_file_upload")
    private int ticketMaximumFileUpload;
    
}
