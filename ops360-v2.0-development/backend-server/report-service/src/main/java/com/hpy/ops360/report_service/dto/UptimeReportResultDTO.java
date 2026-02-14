package com.hpy.ops360.report_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UptimeReportResultDTO extends GenericDto{
   
	@JsonIgnore
	private Long id;
	
    private Integer srno;

    private String userid;

    private Double transactionAchieved;

    private Double transactionTarget;

    private Integer uptimeTarget;

    private Double uptimeAchieved;

    private Integer atmCount;

    private String fullName;

    private String emailId;

    private String mobileNo;

	public UptimeReportResultDTO(Integer srno, String userid, Double transactionAchieved, Double transactionTarget,
			Integer uptimeTarget, Double uptimeAchieved, Integer atmCount, String fullName, String emailId,
			String mobileNo) {
		super();
		this.srno = srno;
		this.userid = userid;
		this.transactionAchieved = transactionAchieved;
		this.transactionTarget = transactionTarget;
		this.uptimeTarget = uptimeTarget;
		this.uptimeAchieved = uptimeAchieved;
		this.atmCount = atmCount;
		this.fullName = fullName;
		this.emailId = emailId;
		this.mobileNo = mobileNo;
	}

    
}
