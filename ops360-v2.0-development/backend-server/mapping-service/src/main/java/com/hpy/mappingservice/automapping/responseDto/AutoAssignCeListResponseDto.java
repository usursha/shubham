package com.hpy.mappingservice.automapping.responseDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AutoAssignCeListResponseDto extends GenericDto{
    
	@JsonIgnore
	private Long id;
    @JsonProperty("ce_user_id")
    private String ceUserId;
    
    @JsonProperty("ce_user_name")
    private String ceUserName;
    
    @JsonProperty("city")
    private String city;
    
    @JsonProperty("employee_code")
    private String employeeCode;
    
    @JsonProperty("home_address")
    private String homeAddress;
    
    @JsonProperty("atm_count")
    private Integer atmCount;
    
    @JsonProperty("mapped_atm")
    private Integer mappedAtm;
    
    @JsonProperty("remaining")
    private Integer remaining;
}