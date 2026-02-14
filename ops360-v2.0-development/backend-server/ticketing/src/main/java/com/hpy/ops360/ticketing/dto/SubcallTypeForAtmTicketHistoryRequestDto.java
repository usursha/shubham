package com.hpy.ops360.ticketing.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubcallTypeForAtmTicketHistoryRequestDto {

    private String broadCategory;
}
