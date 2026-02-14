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

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class AllocationETADetails {

	@Id
	private Long srno;
	private String allocation_type;
	private Timestamp created_date;
	private String allocation_owner;
	private String subcall_type;
	
	private String follow_up;
	private String status;
}

