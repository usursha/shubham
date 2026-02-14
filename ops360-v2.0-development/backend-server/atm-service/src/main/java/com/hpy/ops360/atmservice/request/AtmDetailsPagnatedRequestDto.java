package com.hpy.ops360.atmservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AtmDetailsPagnatedRequestDto {

	private int page;
	private int size;

}
