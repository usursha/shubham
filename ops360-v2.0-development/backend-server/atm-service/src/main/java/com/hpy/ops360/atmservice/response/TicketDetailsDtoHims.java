package com.hpy.ops360.atmservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailsDtoHims {
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
	    private int downtimeinmins;
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
	    private String customerRemark;
	
	    public TicketDetailsDtoHims(String srno, String customer, String equipmentid, String model, String atmcategory,
				String atmclassification, String calldate, String createddate, String calltype, String subcalltype,
				int downtimeinmins, String vendor, String servicecode, String diagnosis, String eventcode,
				String helpdeskname, String lastallocatedtime, String lastcomment, String status, String substatus,
				String ro, String site, String address, String city, String locationname, String state,
				String nextfollowup, String etadatetime, String owner, String customerRemark) {
			super();
			this.srno = srno;
			this.customer = customer;
			this.equipmentid = equipmentid;
			this.model = model;
			this.atmcategory = atmcategory;
			this.atmclassification = atmclassification;
			this.calldate = calldate;
			this.createddate = createddate;
			this.calltype = calltype;
			this.subcalltype = subcalltype;
			this.downtimeinmins = downtimeinmins;
			this.vendor = vendor;
			this.servicecode = servicecode;
			this.diagnosis = diagnosis;
			this.eventcode = eventcode;
			this.helpdeskname = helpdeskname;
			this.lastallocatedtime = lastallocatedtime;
			this.lastcomment = lastcomment;
			this.status = status;
			this.substatus = substatus;
			this.ro = ro;
			this.site = site;
			this.address = address;
			this.city = city;
			this.locationname = locationname;
			this.state = state;
			this.nextfollowup = nextfollowup;
			this.etadatetime = etadatetime;
			this.owner = owner;
			this.customerRemark = customerRemark;
		}
	
	
	    
	
}
