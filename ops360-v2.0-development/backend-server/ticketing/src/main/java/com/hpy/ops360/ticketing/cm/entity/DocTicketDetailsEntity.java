package com.hpy.ops360.ticketing.cm.entity;

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
public class DocTicketDetailsEntity {
	@Id
	@Column(name = "id")
	private Integer sr;
    private String atm_id;
    private String ticket_number;
    private String created_date;
    private String error;
    private String remark;
    private String document;
}

