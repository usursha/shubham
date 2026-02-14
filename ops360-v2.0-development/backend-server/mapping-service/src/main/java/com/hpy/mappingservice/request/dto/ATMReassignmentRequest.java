package com.hpy.mappingservice.request.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATMReassignmentRequest {
   // private String cmUserId;
    private String leavingCeUserId;
}