package com.hpy.ops360.ticketing.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HimsCreateTicketRequest {
    private String atmid;
    private String refno;
    private String diagnosis;
    private String createdby;
    private String comment;
   // private String eventcode;
}