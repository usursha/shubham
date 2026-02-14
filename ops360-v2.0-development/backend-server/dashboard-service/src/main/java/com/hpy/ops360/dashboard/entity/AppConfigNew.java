package com.hpy.ops360.dashboard.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "app_config_v1")
public class AppConfigNew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer srNo;
    
    @Column(name="app_version")
    private String version;

    private String apkUrl;
    private Integer locationFrequency;
    private Integer apiFrequency;
    private Boolean enableLog;
    private Boolean enableScreenshot;
    private Boolean enableDeveloperMode;
    private Integer maximumFileSize;
    private Integer ticketMaximumFileUpload;

    @Column(name = "created_on")
    private LocalDateTime createdDate;
}
