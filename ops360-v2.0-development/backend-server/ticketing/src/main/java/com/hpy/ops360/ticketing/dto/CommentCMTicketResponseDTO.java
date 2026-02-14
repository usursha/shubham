package com.hpy.ops360.ticketing.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CommentCMTicketResponseDTO extends GenericDto {

	private static final long serialVersionUID = 1L;
	private boolean success;
	private String message;
}
