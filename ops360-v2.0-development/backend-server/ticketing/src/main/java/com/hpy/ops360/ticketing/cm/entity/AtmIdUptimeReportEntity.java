package com.hpy.ops360.ticketing.cm.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AtmIdUptimeReportEntity {

	@Id
    @JsonProperty("sr_no")
    private Long srNo;

    @JsonProperty("date")
    private String date;

    @JsonProperty("atmid_current")
    private String atmidCurrent;

    @JsonProperty("bank")
    private String bank;

    @JsonProperty("old_atm_ids")
    private String oldAtmIds;

    @JsonProperty("ps_id")
    private String psId;

    @JsonProperty("atm_location")
    private String atmLocation;

    @JsonProperty("city")
    private String city;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zone")
    private String zone;

    @JsonProperty("site_type")
    private String siteType;

    @JsonProperty("live_date")
    private String liveDate;

    @JsonProperty("zonal_head")
    private String zonalHead;

    @JsonProperty("zonal_head_user_id")
    private String zonalHeadUserId;

    @JsonProperty("zonal_head_mobile_number")
    private String zonalHeadMobileNumber;

    @JsonProperty("zonal_head_email_id")
    private String zonalHeadEmailId;

    @JsonProperty("scm_name")
    private String scmName;

    @JsonProperty("scm_user_id")
    private String scmUserId;

    @JsonProperty("scm_mobile_number")
    private String scmMobileNumber;

    @JsonProperty("scm_email_id")
    private String scmEmailId;

    @JsonProperty("cm_name")
    private String cmName;

    @JsonProperty("cm_user_id")
    private String cmUserId;

    @JsonProperty("cm_mobile_number")
    private String cmMobileNumber;

    @JsonProperty("cm_email_id")
    private String cmEmailId;

    @JsonProperty("ce_name")
    private String ceName;

    @JsonProperty("ce_user_id")
    private String ceUserId;

    @JsonProperty("ce_mobile_number")
    private String ceMobileNumber;

    @JsonProperty("ce_mail_id")
    private String ceMailId;

    @JsonProperty("target")
    private String target;
    
    @Column(name="mtd_till_31st_Oct_24")
    private String mtdTill31stOct24;

    @JsonProperty("mtd_transactions_achieved_percent")
    private Float mtdTransactionsAchievedPercent;

    @JsonProperty("mtd_uptime")
    private String mtdUptime;
}
