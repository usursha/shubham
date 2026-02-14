package com.MapPUC.dto;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserdistLocationRequest {
	private String username;
	private LocalDate created_on;
}