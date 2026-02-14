package com.hpy.ops360.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true)
public class HpyMatrixDto {

	private static final long serialVersionUID = -6433666576874657197L;
	private String contactPerson;
	private String category;
	private String email;
	private String phoneNumber;
}
