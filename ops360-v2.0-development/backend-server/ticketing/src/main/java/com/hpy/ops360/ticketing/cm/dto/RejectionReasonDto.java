package com.hpy.ops360.ticketing.cm.dto;

import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RejectionReasonDto extends GenericDto {

	private static final long serialVersionUID = 1L;

	private String rejectionReason;
}
