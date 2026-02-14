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
public class AllocationDetailsDto extends GenericDto {

	@JsonIgnore
	private Long id;

	private Long srno;
	private String final_allocation_type;
	private Timestamp created_date; //format should be this 13 March ‘24, 02:30 PM
	private String createfilter;
	private String allocation_owner;
	private String subcall_type;
	private String follow_up;
	private String status;
	
	public AllocationDetailsDto(Long srno, String final_allocation_type, Timestamp created_date, String createfilter,
			String allocation_owner, String subcall_type, String follow_up, String status) {
		super();
		this.srno = srno;
		this.final_allocation_type = final_allocation_type;
		this.created_date = created_date;
		this.createfilter = createfilter;
		this.allocation_owner = allocation_owner;
		this.subcall_type = subcall_type;
		this.follow_up = follow_up;
		this.status = status;
	}
	 

	// Custom getter to return formatted date
    public String getCreated_date() {
        if (created_date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM ''yy, hh:mm a");
            return formatter.format(created_date);
        }
        return null;
    }
    
 // Custom getter to return formatted createfilter
    public String getCreatefilter() {
        if (created_date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            return formatter.format(created_date);
        }
        return createfilter;
    }

    // Getter for raw Timestamp
    @JsonIgnore
    public Timestamp getRawCreatedDate() {
        return created_date;
    }



}
