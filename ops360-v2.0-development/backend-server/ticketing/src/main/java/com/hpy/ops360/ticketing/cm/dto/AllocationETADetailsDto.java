//package com.hpy.ops360.ticketing.cm.dto;
//
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.hpy.generic.impl.GenericDto;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class AllocationETADetailsDto extends GenericDto {
//
//	@JsonIgnore
//	private Long id;
//
//	private Long srno;
//	private String allocation_type;
//	private Timestamp created_date; //format should be this 13 March ‘24, 02:30 PM
//	private String allocation_owner;
//	private String subcall_type;
//	private String follow_up;
//	private String status;
//	
//
//	 // Custom getter to return formatted date
//    public String getCreated_date() {
//        if (created_date != null) {
//            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM ''yy, hh:mm a");
//            return formatter.format(created_date);
//        }
//        return null;
//    }
//}



package com.hpy.ops360.ticketing.cm.dto;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllocationETADetailsDto extends GenericDto {

	public AllocationETADetailsDto(Long srno, String allocation_type, Timestamp created_date, String allocation_owner,
			String subcall_type, String follow_up, String status) {
		super();
		this.srno = srno;
		this.allocation_type = allocation_type;
		this.created_date = created_date;
		this.allocation_owner = allocation_owner;
		this.subcall_type = subcall_type;
		this.follow_up = follow_up;
		this.status = status;
	}




	@JsonIgnore
	private Long id;

	private Long srno;
	private String allocation_type;
	private Timestamp created_date; //format should be this 13 March ‘24, 02:30 PM
	private String allocation_owner;
	private String subcall_type;
	private String follow_up;
	private String status;
	
	
	
	
	 // Custom getter to return formatted date
    public String getCreated_date() {
        if (created_date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM ''yy, hh:mm a");
            return formatter.format(created_date);
        }
        return null;
    }
}
