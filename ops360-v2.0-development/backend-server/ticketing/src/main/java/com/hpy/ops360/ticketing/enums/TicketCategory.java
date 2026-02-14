package com.hpy.ops360.ticketing.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TicketCategory {

	INFRA("INFRA", "Infra"), GENERAL("GENERAL", "General"), CE_MANUAL("CE_MANUAL", "Manual");

	private String key;
	private String value;
}
