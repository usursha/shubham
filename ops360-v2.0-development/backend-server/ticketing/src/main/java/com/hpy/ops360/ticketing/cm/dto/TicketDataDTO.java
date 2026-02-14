package com.hpy.ops360.ticketing.cm.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDataDTO {
    private Integer srno;
    private String ticketNumber;
    private Integer status;
    private String ticketType;
    private Integer etaExpired;
    private Date createdDate;
    
}


