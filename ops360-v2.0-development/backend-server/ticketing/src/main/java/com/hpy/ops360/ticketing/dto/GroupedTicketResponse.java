package com.hpy.ops360.ticketing.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupedTicketResponse extends GenericDto {

	
	@JsonIgnore
	private Long id;
	
    private String date;
    private List<GetManuallyTicketResponseDTO> data;    

    
 // Custom constructor to initialize only date and data fields
    public GroupedTicketResponse(String date, List<GetManuallyTicketResponseDTO> data) {
        this.date = date;
        this.data = data;
    }
}
