package com.hpy.ops360.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CeUserDetailsDto  {
	
    private String ceUserId;

    private String ceName;

    private String ceEmail;

    private String ceMobile;

}
