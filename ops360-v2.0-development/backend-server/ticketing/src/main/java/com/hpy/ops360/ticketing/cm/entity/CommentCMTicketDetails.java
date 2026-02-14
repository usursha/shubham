package com.hpy.ops360.ticketing.cm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CommentCMTicketDetails {

	@Id
	private String atmId;
	private String ticketNumber;
	private String createBy;
	private String cmComments;
	private String etaDateTime;
	private String createDateTime;
	private String modifyDateTime;

}
