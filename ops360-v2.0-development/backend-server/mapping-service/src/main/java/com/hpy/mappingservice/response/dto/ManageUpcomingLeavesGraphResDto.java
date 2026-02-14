package com.hpy.mappingservice.response.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageUpcomingLeavesGraphResDto {
    private String primaryCeUserName;
    private List<ManageUpcomingLeavesGraphResDto2> temporaryExecutives;
}

