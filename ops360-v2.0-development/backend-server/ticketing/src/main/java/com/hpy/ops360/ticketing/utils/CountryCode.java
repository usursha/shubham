package com.hpy.ops360.ticketing.utils;

import org.springframework.stereotype.Component;

@Component
public class CountryCode {
	
	public static String addCountryCode(String mobileNumber)
	{
		return "+91"+mobileNumber;
	}

}
