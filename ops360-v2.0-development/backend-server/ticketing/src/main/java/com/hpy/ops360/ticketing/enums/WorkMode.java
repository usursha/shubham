package com.hpy.ops360.ticketing.enums;

import lombok.Getter;

public enum WorkMode {

	WIP_AT_WORK("WIP_AT_WORK", "WIP at work"), LEGAL_POLICE("LEGAL_POLICE", "Legal/Police");

	@Getter
	String key;
	@Getter
	String value;

	private WorkMode(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
