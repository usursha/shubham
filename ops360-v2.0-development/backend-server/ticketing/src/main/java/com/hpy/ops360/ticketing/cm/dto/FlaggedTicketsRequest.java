package com.hpy.ops360.ticketing.cm.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlaggedTicketsRequest {
    private String userId;   // Accept comma-separated user IDs
    private String userType; // CE or CM
}
