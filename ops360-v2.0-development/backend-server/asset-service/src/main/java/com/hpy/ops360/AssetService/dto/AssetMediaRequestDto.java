package com.hpy.ops360.AssetService.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetMediaRequestDto {
	
	@NotEmpty(message = "atmId cannot be null!!")
	private String atmId;
	@NotEmpty(message = "ticketId cannot be null!!")
	private String ticketId;

}
