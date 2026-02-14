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
public class SecondaryCeDetailsResponseDto extends GenericDto{
	
	    @JsonIgnore
	    private Long id;
	    
	    private String ce_full_name;
	    
	    private String ce_profile;
	    
	    private String ce_id;
	    
	    private String ce_user_id;
	    
	    private String employee_id;
	    
	//    private String area;
	    
	    private String address;
	    
	    private String city;
	    
	    private Integer assigned_atm;
	    
	    private Integer mapped_atm;
	    
	    private Integer remaining_atm;
	    
	    private String atm_distance;

	    
	
}
