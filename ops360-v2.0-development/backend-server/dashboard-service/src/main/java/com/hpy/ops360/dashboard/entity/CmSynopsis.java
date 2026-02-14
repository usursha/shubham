package com.hpy.ops360.dashboard.entity;

import com.hpy.generic.impl.GenericEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
public class CmSynopsis extends GenericEntity {

	
//	@Id
//	private String id;

	@Column(name = "current_status")
	private String currentStatus;

	@Column(name = "time_laps")
	private String timeLaps;

	@Column(name = "flag_status")
	private Integer flagStatus;

	@Column(name = "channel_executive_name")
	private String channelExecutiveName;

	@Column(name = "total_uptime")
	private String totalUptime;

	@Column(name = "down_machines")
	private String downMachines;

	@Column(name = "visit_completion")
	private String visitCompletion;

	@Column(name = "distance_covered")
	private String distanceCovered;

	@Column(name = "count_flag_status")
	private Integer countFlagStatus;

	@Column(name = "ticketex")
	private Integer ticketEx;

	@Column(name = "trgt_colour_code")
	private String trgtColourCode;

	@Column(name = "sampathi_trgt")
	private String sampathiTrgt;

	@Column(name = "ce_username")
	private String CeUserName;

//    @Column(name = "is_active")
//    private boolean isActive;
}
