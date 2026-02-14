package com.hpy.ops360.ticketing.route;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "get_open_ticket")
public class GetOpenTicket {

    @Id
    private String srno;

    private String requestid;
    private String atmid;
   
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
    private String downtimeinmins;
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

    // Getters and setters omitted for brevity
}
