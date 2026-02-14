package com.hpy.mappingservice.response.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;
import com.hpy.mappingservice.entity.ManageUpcomingLeavesGraphEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManageUpcomingLeavesResDto2 extends GenericDto {
    private List<ManageUpcomingLeavesCeResDto> userData;
    private List<ManageUpcomingLeavesGraphResDto> graphdata;

    public ManageUpcomingLeavesResDto2(List<ManageUpcomingLeavesCeResDto> userData, List<ManageUpcomingLeavesGraphResDto> graphdata) {
        this.userData = userData;
        this.graphdata = graphdata;
    }
}
