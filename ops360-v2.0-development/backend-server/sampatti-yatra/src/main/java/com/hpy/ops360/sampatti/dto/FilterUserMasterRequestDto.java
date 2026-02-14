// FilterUserMasterRequestDto.java
package com.hpy.ops360.sampatti.dto;

import lombok.Data;

@Data
public class FilterUserMasterRequestDto {
    private String zoneList;
    private String stateList;
    private String cityList;
    private String designation;
}
