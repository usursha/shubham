package com.hpy.ops360.ticketing.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HimsUpdateFollowUpDto {
    
    @JsonProperty("ticketid")
    private String ticketid;
    
    @JsonProperty("atmid")
    private String atmid;
    
    @JsonProperty("etadatetime")
    private String etadatetime;
    
    @JsonProperty("owner")
    private String owner;
    
    @JsonProperty("subcalltype")
    private String subcalltype;
    
    @JsonProperty("customerRemark")
    private String customerRemark;
    
    @JsonProperty("updatedby")
    private String updatedby;
    
    @JsonProperty("comment")
    private String comment;
}