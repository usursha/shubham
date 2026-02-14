package com.hpy.mappingservice.response.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExitedCeMappingResponseDto extends GenericDto{
	
	    @JsonIgnore
	    private Long id;
	    
	    @JsonProperty("ce_full_name")
	    private String ceFullName;
	    
	    @JsonProperty("ce_profile")
	    private String ceProfile;
	    
	    @JsonProperty("ce_user_id")
	    private String ceUserId;
	    
	    @JsonProperty("ce_id")
	    private String ceId;
	    
	    @JsonProperty("employee_id")
	    private String employeeId;
	    
	    @JsonProperty("exited_date")
	    private String exitedDate;
	    
	    @JsonProperty("address")
	    private String address;
	    
	    @JsonProperty("assigned_atm")
	    private Integer assignedAtm;
	    
	    @JsonProperty("mapped_atm")
	    private Integer mappedAtm;
	    
	    @JsonProperty("mapped_atm_percentage")
	    private String mappedAtmPercentage;
	    
	    @JsonProperty("leave_id")
	    private String leaveId;

	    
	
}
