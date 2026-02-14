package com.hpy.ops360.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_telemetry")
public class UserTelemetry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "journey_id")
	private String journeyId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "screen")
	private String screen;

	@Column(name = "action")
	private String action;
	
	@Column(name = "device_model")
	private String deviceModel;
	
	@Column(name = "os_version")
	private String osVersion;

	@Column(name = "insert_date_time")
	private String insertDateTime;

}
