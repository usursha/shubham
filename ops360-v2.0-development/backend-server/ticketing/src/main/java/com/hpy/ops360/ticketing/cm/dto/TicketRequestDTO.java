package com.hpy.ops360.ticketing.cm.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {
    private String cmUserId;
    private Integer pageSize ;
    private Integer pageNumber ;
    private String flag;
    private String searchText;
}