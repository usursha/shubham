package com.hpy.mappingservice.response.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrimaryCeAtmDetailsResponseDto extends GenericDto{
	
	    @JsonIgnore
	    private Long id;
	    
	    @JsonProperty("atm_id")
	    private String atmId;
	    
	    @JsonProperty("bank_name")
	    private String bankName;
	    
	    @JsonProperty("address")
	    private String address;
	    
	    @JsonProperty("assigned_ce")
	    private String assignedCe;
	    
	    @JsonProperty("dist_from_base")
	    private String distFromBase;
	    
	    @JsonProperty("status")
	    private String status;
	    
	    @JsonProperty("city")
	    private String city;

}
