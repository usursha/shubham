package com.hpy.ops360.atmservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AtmIndent {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "atm_code")
	private String atmCode;

	@Column(name = "account")
	private String account;

	@Column(name = "Cashfilldate")
	private String cashFillDate;

	@Column(name = "indent_total")
	private String indentTotal;

	@Column(name = "CRA")
	private String cra;

}
