package com.hpy.ops360.atmservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CeWiseAtmPaginationRequestDTO {
	private String userId;
    private int pageNumber;
    private int pageSize;
    private String sortOption;
    private String bank;
    private String grade;
    private String status;
    private String uptimeStatus;
    private String searchText; 
}