package com.hpy.ops360.ticketing.cm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryRequestDto {
    private String cm_user_id;      
    private Integer pageNumber;
    private Integer pageSize;
    private String flag;
    private String searchText;
    
}
