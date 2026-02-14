package com.hpy.ops360.atmservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CeWiseAtmSearchRequestDTO {
	private String userId;
    private String sortOption;
    private String bank;
    private String grade;
    private String status;
    private String uptimeStatus;

}