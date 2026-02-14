package com.hpy.ops360.dashboard.entity;

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
public class HelpDeskHandlerDetails {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "hde_user_id")
	private String handlerId;

	@Column(name = "mobileno")
	private String mobileNo;

}
