package com.hpy.ops360.atmservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailEntity {

    // Ticket details from CombinedTickets
	@Id
    private String srno;
    private String customer;
    private String equipmentid;
    private String model;
    private String atmcategory;
    private String atmclassification;
    private Timestamp calldate;
    private Timestamp createddate;
    private String calltype;
    private String subcalltype;
    private Timestamp completiondatewithtime;
    private Integer downtimeinmins;
    private String vendor;
    private String servicecode;
    private String diagnosis;
    private String eventcode;
    private String helpdeskname;
    private Timestamp lastallocatedtime;
    private String lastcomment;
    private String lastactivity;
    private String status;
    private String substatus;
    private String ro;
    private String site;
    private String address;
    private String city;
    private String locationname;
    private String state;
    private Timestamp nextfollowup;
    private Timestamp etadatetime;
    private String owner;
    private String customer_remark;
    private String ticket_source;
    private Integer priority_order;
    private String atm_status;

    // Additional fields from the final SELECT
    private String formatted_downtime;
    private Integer ageing_days;
    private String ce_user_id;
    private String flag_status;
}