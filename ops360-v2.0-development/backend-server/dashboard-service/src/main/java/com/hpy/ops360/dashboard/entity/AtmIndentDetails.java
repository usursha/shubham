package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class AtmIndentDetails {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "atm_code")
	private String atmCode;

	@Column(name = "account")
	private String account;

	@Column(name = "Cashfilldate")
	private String cashFillDate;

	@Column(name = "Cash_100")
	private String cash100;

	@Column(name = "Cash_200")
	private String cash200;

	@Column(name = "Cash_500")
	private String cash500;

	@Column(name = "Cash_2000")
	private String cash2000;

	@Column(name = "indent_total")
	private String indentTotal;

	@Column(name = "CRA")
	private String cra;
}
