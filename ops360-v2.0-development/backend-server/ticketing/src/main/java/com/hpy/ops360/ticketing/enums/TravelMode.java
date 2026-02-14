package com.hpy.ops360.ticketing.enums;

import lombok.Getter;

public enum TravelMode {
	BIKE("BIKE", "Bike"), BUS("BUS", "Bus"), TRAIN("TRAIN", "Train"), CAB("CAB", "Cab"), AUTO("AUTO", "Auto");

	@Getter
	String key;
	@Getter
	String value;

	private TravelMode(String key, String value) {
		this.key = key;
		this.value = value;
	}
}
