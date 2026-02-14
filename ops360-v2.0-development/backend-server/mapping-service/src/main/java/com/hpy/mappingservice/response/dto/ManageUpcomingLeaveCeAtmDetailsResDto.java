package com.hpy.mappingservice.response.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;
import com.hpy.mappingservice.request.dto.ManageUpcomingLeaveCeAtmDetailsReqDto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManageUpcomingLeaveCeAtmDetailsResDto extends GenericDto {
	
	@JsonIgnore
    private Integer srNo;
    
    private String atm_code;
    
    private String bankName;
    
    private String address;
    
    private String city;
    
    private String dist_from_base;
    
    private String temp_ce_id;
    
    private String temp_ce_fullName;

}
