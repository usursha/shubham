package com.hpy.ops360.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
//@Table(name = "prompt_mast")
public class Prompt {

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sr_no")
	private Integer id;

	@Column(name = "event_code")
	private String eventCode;

	@Column(name = "prompt_no")
	private int promptNo;

	@Column(name = "prompt_desc")
	private String promptDescription;

//	@Column(name = "latitude")
//	private String latitude;
//
//	@Column(name = "longitude")
//	private String longitude;

//	@Column(name = "create_time")
//	private LocalDateTime createTime;

}
