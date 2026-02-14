package com.hpy.sampatti_data_service.response;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CMUnderCEAtmDetailsDTO {

	private Long id;

	private String atmid;

	private String bankname;

	private String grade;

	private String address;

	private String machineStatus;

	private Integer openTickets;

	private String errorCategory;

	private String ownership;

	private String ticketAging;

	private String transactionTrend;

	private String mtdPerformance;

	private int uptimeTrend;

	 private String uptimeStatus;

	private String mtdUptime;

	private String nameOfChannelExecutive;

	private String nameOfSecondaryChannelExecutive;

	private String lastVisitedOn;

	// Getters and Setters
}
