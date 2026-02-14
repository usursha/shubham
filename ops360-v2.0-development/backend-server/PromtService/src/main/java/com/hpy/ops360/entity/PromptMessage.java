package com.hpy.ops360.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PromptMessage {

	@Id
	@Column(name = "sr_no")
	private Long srNo;

	@Column(name = "prompt")
	private String promptMsg;

}
