package com.hpy.ops360.ticketing.dto;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtaDto extends UserLocationDto {
	private static final long serialVersionUID = 7853969567278052541L;

	private String ticketNumber;

	private String atmid;

	private String owner;

	private String customerRemark;

	private String internalRemark;

	private String etaDateTime;

	private String documentName;

	@Exclude
	private String document;

//	private EtaDocumentDto etaDocumentDto;

	private String createdBy;

	private String lastModifiedBy;

}
