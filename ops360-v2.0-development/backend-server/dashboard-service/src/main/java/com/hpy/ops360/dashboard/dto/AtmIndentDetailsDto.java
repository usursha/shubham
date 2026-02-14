package com.hpy.ops360.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hpy.generic.impl.GenericDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class AtmIndentDetailsDto extends GenericDto{

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;
	
	private Long srNo;

	private String atmCode;

	private String account;

	private String cashFillDate;

	private String cash100;

	private String cash200;

	private String cash500;

	private String cash2000;

	private String indentTotal;

	private String cra;

	public AtmIndentDetailsDto(Long srNo, String atmCode, String account, String cashFillDate, String cash100,
			String cash200, String cash500, String cash2000, String indentTotal, String cra) {
		super();
		this.srNo = srNo;
		this.atmCode = atmCode;
		this.account = account;
		this.cashFillDate = cashFillDate;
		this.cash100 = cash100;
		this.cash200 = cash200;
		this.cash500 = cash500;
		this.cash2000 = cash2000;
		this.indentTotal = indentTotal;
		this.cra = cra;
	}
	
	
}
