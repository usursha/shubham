package com.hpy.ops360.atmservice.response;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketRaisedResponseByAtm extends GenericDto{

	
	List<TicketRaisedCategoriesByAtmResponseDto> data;
}
