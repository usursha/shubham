package com.hpy.mappingservice.automapping.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AutoAssignEntity {

	@Id
//	private Long id;

	@Column(name = "atm_code")
	private String atmCode;

	@Column(name = "address")
	private String homeAddress;

	@Column(name = "new_ce_user_id")
	private String newCeUserId;

	@Column(name = "full_name")
	private String fullName;
	
	@Column(name = "new_ce_id")
	private Long newCeId;

	@Column(name = "distance")
	private Double distance;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "status")
	private LocalDateTime status;

	@Column(name = "city")
	private LocalDateTime city;

}
