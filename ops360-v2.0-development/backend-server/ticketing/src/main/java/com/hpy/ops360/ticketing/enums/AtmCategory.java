package com.hpy.ops360.ticketing.enums;

import lombok.Getter;

@Getter
public enum AtmCategory {

	SUPER_PLATINUM(1), PLATINUM(2), GOLD(3), SILVER(4), BRONZE(5), NEW(6);

	private final int atmCategoryCode;

	AtmCategory(int atmCategoryCode) {
		this.atmCategoryCode = atmCategoryCode;
	}

}
