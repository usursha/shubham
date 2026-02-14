package com.hpy.ops360.atmservice.entity;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignedAtmDetailsEntityHIMS {
    
    @Id
    private String atmId;
	private String address;
	private double mtdUptime;
	private String latestticketNo;
	private String lastVisited;
	private int isBreakdownCount;
	private String machineMaxDowntime;

    @Embedded
    private TicketCategoryCount ticketCategoryCount;

    @ElementCollection
    private List<TicketHistory> ticketHistories;

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TicketCategoryCount {
	
	    	private Integer total;
	    	private Integer cash;
	    	private Integer communication;
	    	private Integer hardwareFault;
	    	private Integer supervisory;
	    	private Integer others;
    }

    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TicketHistory {

    	private String atmId;// equipmentid
    	private String ticketNumber; // srno
    	private String owner; // broad category
    	private String issue; // eventcode
    	private String calldate; // calldate
    	private String downTime; // downtimeinmins
    	private String subcall; //subcalltype
    }


}
