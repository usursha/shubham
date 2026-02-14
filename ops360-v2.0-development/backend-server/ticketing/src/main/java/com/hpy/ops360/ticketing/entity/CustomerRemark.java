package com.hpy.ops360.ticketing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "customer_remark")
//@EqualsAndHashCode(callSuper = true)
public class CustomerRemark {
	@Id
	@Column(name = "SR_NO")
	private Long srNo;

	@Column(name = "remarks")
	private String remarks;
}
