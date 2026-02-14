package com.hpy.ops360.ticketing.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtmTicketEvent {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "atm_id")
	private String atmId;

	@Column(name = "ticket_id")
	private String ticketId;

	@Column(name = "eventcode")
	private String eventCode;

	@Column(name = "priority_score")
	private Double priorityScore;

	@Column(name = "eventgroup")
	private String eventGroup;

	@Column(name = "isbreakdown")
	private Integer isBreakdown;

	@Column(name = "is_updated")
	private Integer isUpdated;

	@Column(name = "is_timed_out")
	private Integer isTimedOut;

	@Column(name = "is_travelling")
	private Integer isTravelling;

	@Column(name = "travel_time")
	private Date travelTime;

	@Column(name = "travel_eta")
	private Integer travelEta;

	@Column(name = "down_call")
	private Integer downCall;

	@Column(name = "eta_date_time")
	private String etaDateTime;

	@Column(name = "owner")
	private String owner;

	@Column(name = "subcall_type")
	private String subcall;

	@Column(name = "eta_timeout")
	private String etaTimeout;
	
	@Column(name = "internal_remark")
	private String internalRemark;

	// ---added column ----
	@Column(name = "flag_status")
	private int flagStatus;

	@Column(name = "flag_status_inserttime")
	private LocalDateTime flagStatusInsertTime;
	
	@Column(name="ce_name")
	private String ceName;
	
	@Column(name="ce_full_name")
	private String ceFullName;

	private static final DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
	public String getFormattedEtaDateTime() {
		if (etaDateTime == null || etaDateTime.trim().isEmpty()) {
			return "";
		}
		try {
			LocalDateTime dateTime = LocalDateTime.parse(etaDateTime, inputFormatter);
			return dateTime.format(outputFormatter);
		} catch (DateTimeParseException e) {

			return etaDateTime;
		}
	}

}
