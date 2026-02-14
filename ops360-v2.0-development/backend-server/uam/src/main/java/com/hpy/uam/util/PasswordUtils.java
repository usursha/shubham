package com.hpy.uam.util;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtils {

	private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);
	
	private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
	
	public Boolean validatePasswordPattern(String pwd) {
		return true; // (pwd.matches(PASSWORD_PATTERN));

	}

//	generateRandomPassword method
	public String generatedRandomPassword() {
		int passwordLength = 8;

		// Define the characters that can be used in the random password
		String allowedChars = PASSWORD_PATTERN;

		StringBuilder password = new StringBuilder();

		Random random = new Random();
		for (int i = 0; i < passwordLength; i++) {
			int randomIndex = random.nextInt(allowedChars.length());
			password.append(allowedChars.charAt(randomIndex));
		}
		log.info("generated password {}", password.toString());
		return password.toString();
	}
}
