package com.hpy.ops360.atmservice.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankNameAndAtmUpTimeEntity {

	@Id
	@Column(name="sr_no")
	private int srNo;
	@Column(name="bank_name")
	private String bankName;
//	private String monthtotilldateuptime;
	@Column(name="mtd_uptime")
	private BigDecimal mtdUptime;
}
