package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignedAtmDetailsEntityHIMSDto {

	 private String atmId;
	    private String address;
	    private double mtdUptime;
	    private String lastestticketNo;
	    private String lastVisited;
	    private int isBreakdownCount;
	    private String machineMaxDowntime;
	    private TicketCategoryCount ticketCategoryCount;
	    private List<TicketHistory> ticketHistories;

	    @Data
	    @AllArgsConstructor
	    @NoArgsConstructor
	    public static class TicketCategoryCount {
	        private int total;
	        private int cash;
	        private int communication;
	        private int hardwareFault;
	        private int supervisory;
	        private int others;
	    }

	    @Data
	    @AllArgsConstructor
	    @NoArgsConstructor
	    public static class TicketHistory {
	        private String atmId; // equipmentid
	        private String ticketNumber; // srno
	        private String owner; // broad category
	        private String issue; // eventcode
	        private String calldate; // calldate
	        private String downTime; // downtimeinmins
	        private String subcall; // subcalltype
	    }
}
