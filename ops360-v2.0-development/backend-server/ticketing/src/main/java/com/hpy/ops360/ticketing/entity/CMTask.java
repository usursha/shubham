package com.hpy.ops360.ticketing.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class CMTask implements Serializable {

	private static final long serialVersionUID = -3328590094816811088L;

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "atm_id")
	private String atmId;

	@Column(name = "comment")
	private String comment;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "diagnosis")
	private String diagnosis;

	@JsonIgnore
	@Column(name = "document")
	private String document;

	@Column(name = "document_name")
	private String documentName;

	@Column(name = "reason")
	private String reason;

	@Column(name = "ticket_number")
	private String ticketNumber;

	@Column(name = "status")
	private String status;

	@Column(name = "ref_no")
	private String refNo;

//	@Column(name = "created_date")
//	private Timestamp created_date;

	@Column(name = "created_date")
	private String createdDate;

	@Column(name = "username")
	private String username;

	@Column(name = "document1_name")
	private String document1Name;

	@Column(name = "document2_name")
	private String document2Name;

	@Column(name = "document3_name")
	private String document3Name;

	@Column(name = "document4_name")
	private String document4Name;

//	public String getFormattedCreatedDate() {
//		if (createdDate != null && !createdDate.isEmpty()) {
//			try {
//				// Assuming the createdDate is in the format "dd MMM yyyy hh:mm a"
//				SimpleDateFormat inputFormatter = new SimpleDateFormat("dd MMM yyyy hh:mm a");
//				SimpleDateFormat outputFormatter = new SimpleDateFormat("dd MMMM yyyy hh:mm a");
//
//				// Parse the string into a Date object
//				Date date = inputFormatter.parse(createdDate);
//
//				// Return the formatted date
//				return outputFormatter.format(date);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return createdDate; // Return the original if formatting fails
//	}

}
