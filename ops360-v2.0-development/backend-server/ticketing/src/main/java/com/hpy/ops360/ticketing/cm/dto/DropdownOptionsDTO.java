package com.hpy.ops360.ticketing.cm.dto;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DropdownOptionsDTO extends GenericDto{
	
	@JsonIgnore
	private Long id;
    private List<String> calltype;
	public DropdownOptionsDTO(List<String> calltype) {
		super();
		this.calltype = calltype;
	}
    

}
