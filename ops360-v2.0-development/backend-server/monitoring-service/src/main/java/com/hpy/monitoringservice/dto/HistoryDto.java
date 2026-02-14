package com.hpy.monitoringservice.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDto {
	
    private String date;
    private int count;
    private Set<UsersDto> users;
}