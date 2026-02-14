package com.hpy.ops360.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketCategoryDTO {
	
    private String title;
    private String value;
    private String color;
}
