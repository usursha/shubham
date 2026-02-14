package com.hpy.ops360.atmservice.dto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketFilterDataDto {
    private Integer totalCount;
    private List<TicketsFilterResponseDto> TicketData;
}