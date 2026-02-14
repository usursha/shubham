package com.hpy.ops360.dashboard.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class AtmMtdUptimeDto extends GenericDto {

	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private Long id;
	private String uptime; // mtd
	private Integer atmCount; // updowntotalAtm
	@JsonFormat(pattern = "dd MMMM yyyy, hh:mm a")
	private LocalDateTime timestamp; // Added field for T-1 timestamp

}
