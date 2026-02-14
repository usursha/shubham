package com.hpy.ops360.dashboard.dto;

import java.util.List;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class TicketsRaisedResponseAll extends GenericDto {

	private static final long serialVersionUID = 1L;
	private List<TicketCategoryDTO> data;
}
