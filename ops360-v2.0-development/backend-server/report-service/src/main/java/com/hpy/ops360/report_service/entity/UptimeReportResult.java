package com.hpy.ops360.report_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class UptimeReportResult {
	
    @Id
    @Column(name = "srno")
    private Integer srno;

    @Column(name = "userid")
    private String userid;

    @Column(name = "transactionachieved")
    private Double transactionAchieved;

    @Column(name = "transaction_target")
    private Double transactionTarget;

    @Column(name = "uptime_target")
    private Integer uptimeTarget;

    @Column(name = "uptime_achieved")
    private Double uptimeAchieved;

    @Column(name = "atm_count")
    private Integer atmCount;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "mobile_no")
    private String mobileNo;
    
    @Column(name = "totalcount")
    private Integer totalCount;

    // Getters and setters
}
