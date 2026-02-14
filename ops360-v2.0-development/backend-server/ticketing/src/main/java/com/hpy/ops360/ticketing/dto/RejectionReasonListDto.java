package com.hpy.ops360.ticketing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RejectionReasonListDto extends GenericDto{

	@JsonIgnore
	private Long id;
	
	private Long sr_no;

	private String rejectionReason;

	public RejectionReasonListDto(Long sr_no, String rejectionReason) {
		this.sr_no = sr_no;
		this.rejectionReason = rejectionReason;
	}
	
}
