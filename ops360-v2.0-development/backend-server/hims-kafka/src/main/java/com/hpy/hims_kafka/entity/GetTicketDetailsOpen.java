package com.hpy.hims_kafka.entity;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hpy.hims_kafka.dto.TicketDetailsOpen;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "GetTicketDetails_Open")
public class GetTicketDetailsOpen {

    @Id
    @Column(name = "srno")
    private String srno;

    @Column(name = "customer")
    private String customer;

    @Column(name = "equipmentid")
    private String equipmentid;

    @Column(name = "model")
    private String model;

    @Column(name = "atmcategory")
    private String atmcategory;

    @Column(name = "atmclassification")
    private String atmclassification;

    @Column(name = "calldate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime calldate;

    @Column(name = "createddate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createddate;

    @Column(name = "calltype")
    private String calltype;

    @Column(name = "subcalltype")
    private String subcalltype;

    @Column(name = "completiondatewithtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime completiondatewithtime;

    @Column(name = "downtimeinmins")
    private Integer downtimeinmins;

    @Column(name = "vendor")
    private String vendor;

    @Column(name = "servicecode")
    private String servicecode;

    @Column(name = "diagnosis")
    private String diagnosis;

    @Column(name = "eventcode")
    private String eventcode;

    @Column(name = "helpdeskname")
    private String helpdeskname;

    @Column(name = "lastallocatedtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime lastallocatedtime;

    @Column(name = "lastcomment")
    private String lastcomment;

    @Column(name = "lastactivity")
    private String lastactivity;

    @Column(name = "status")
    private String status;

    @Column(name = "substatus")
    private String substatus;

    @Column(name = "ro")
    private String ro;

    @Column(name = "site")
    private String site;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "locationname")
    private String locationname;

    @Column(name = "state")
    private String state;

    @Column(name = "nextfollowup")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime nextfollowup;

    @Column(name = "etadatetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime etadatetime;

    @Column(name = "owner")
    private String owner;

    @Column(name = "customerRemark")
    private String customerRemark;

    @Column(name = "first_dispatch_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime first_dispatch_datetime;

    @Column(name = "re_allocated_docket_no")
    private String re_allocated_docket_no;

    @Column(name = "lastsubcalltypeby")
    private String lastsubcalltypeby;

    @Column(name = "last_activity_datetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime last_activity_datetime;

    @Column(name = "lastmodifieddatetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime lastmodifieddatetime;

    @Column(name = "username")
    private String username;

    @Column(name = "comment")
    private String comment;

    
    public GetTicketDetailsOpen(TicketDetailsOpen dto) {
        this.srno = dto.getSrno();
        this.customer = dto.getCustomer();
        this.equipmentid = dto.getEquipmentid();
        this.model = dto.getModel();
        this.atmcategory = dto.getAtmcategory();
        this.atmclassification = dto.getAtmclassification();

        this.calldate = dto.getCalldate();
        this.createddate = dto.getCreateddate();

        this.calltype = dto.getCalltype();
        this.subcalltype = dto.getSubcalltype();
        this.completiondatewithtime = dto.getCompletiondatewithtime();
        this.downtimeinmins = dto.getDowntimeinmins();

        this.vendor = dto.getVendor();
        this.servicecode = dto.getServicecode();
        this.diagnosis = dto.getDiagnosis();
        this.eventcode = dto.getEventcode();
        this.helpdeskname = dto.getHelpdeskname();

        this.lastallocatedtime = dto.getLastallocatedtime();
        this.lastcomment = dto.getLastcomment();
        this.lastactivity = dto.getLastactivity();

        this.status = dto.getStatus();
        this.substatus = dto.getSubstatus();

        this.ro = dto.getRo();
        this.site = dto.getSite();
        this.address = dto.getAddress();
        this.city = dto.getCity();
        this.locationname = dto.getLocationname();
        this.state = dto.getState();

        this.nextfollowup = dto.getNextfollowup();
        this.etadatetime = dto.getEtadatetime();

        this.owner = dto.getOwner();
        this.customerRemark = dto.getCustomerRemark();
        this.first_dispatch_datetime = dto.getFirst_dispatch_datetime();
        this.re_allocated_docket_no = dto.getRe_allocated_docket_no();
        this.lastsubcalltypeby = dto.getLastsubcalltypeby();
        this.last_activity_datetime = dto.getLast_activity_datetime();
        this.lastmodifieddatetime = dto.getLastmodifieddatetime();
        this.username = dto.getUsername();
        this.comment = dto.getComment();
    }




}


