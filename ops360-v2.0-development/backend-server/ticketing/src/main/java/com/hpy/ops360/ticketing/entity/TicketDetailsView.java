package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class TicketDetailsView {

    @Id
    private Long rno;

    private String srno;
    private String customer;
    private String equipmentid;
    private String model;
    private String atmcategory;
    private String atmclassification;

    private String calldate;
    private String createddate;

    private String calltype;
    private String subcalltype;
    private String completiondatewithtime;
    private Integer downtimeinmins;

    private String vendor;
    private String servicecode;
    private String diagnosis;
    private String eventcode;
    private String helpdeskname;

    private String lastallocatedtime;
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

    private String nextfollowup;
    private String etadatetime;

    private String owner;

    @Column(name = "customer_remark")
    private String customerRemark;
}

