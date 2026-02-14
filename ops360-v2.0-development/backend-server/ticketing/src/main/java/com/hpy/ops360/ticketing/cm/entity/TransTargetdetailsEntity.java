package com.hpy.ops360.ticketing.cm.entity;

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
public class TransTargetdetailsEntity {

	@Id
	@Column(name = "srno")
	private Long srno;

	@Column(name = "date")
	private String date;

	@Column(name = "userid")
	private String userId;

	@Column(name = "transactiontrend")
	private String transactionTrend;

	@Column(name = "transaction_target")
	private String transactionTarget;

	@Column(name = "percentage_change")
	private Double percentage_change;
}
