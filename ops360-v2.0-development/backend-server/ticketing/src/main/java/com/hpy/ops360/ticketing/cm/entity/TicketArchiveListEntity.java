//package com.hpy.ops360.ticketing.cm.entity;
//
//import java.sql.Timestamp;
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Data
//public class AllocationETADetails {
//
//	@Id
//	private Long srno;
//	private String ticket_number;
//	private String owner;
//	private String subcall_type;
//	private Timestamp last_modified_date;
//	private String last_modified_by;
//	private String allocation_type;
//}

package com.hpy.ops360.ticketing.cm.entity;

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
public class TicketArchiveListEntity {

	@Id
	@Column(name="srno")
	private Long srNo;
	
	@Column(name="bank_name")
	private String bankName;
	
	@Column(name="atm_code")
	private String atmCode;
	
	@Column(name="ticket_number")
	private String ticketNumber;
	
	@Column(name="ce_user_id")
	private String chennaleExecutive; 
	
	
}

