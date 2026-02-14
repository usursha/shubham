package com.hpy.ops360.dashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CEAtmDetailsFor_Cm {

	@Id
	private Long srNo;

	@Column(name = "atm_code")
	private String atmCode;

}
