package com.hpy.ops360.sampatti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaderboardRequestcountDto {
    private String monthYear;     
    private String userType;      
    private String searchKeyword; 
    private String sortOrder;
    private Integer achievedMin;
    private Integer achievedMax;
    private String zone;
    private String state;
    private String city;
}
