package com.hpy.ops360.atmservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class StatusOption {
    private int statusId;
    private String statusName;
    private int statusCount;
}