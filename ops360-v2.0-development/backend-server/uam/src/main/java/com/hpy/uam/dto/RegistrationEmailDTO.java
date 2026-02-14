package com.hpy.uam.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrationEmailDTO {

	@NotNull(message = "Recipient Mail Can Not be empty")
	private String recipient;
	@NotNull(message = "Subject Can Not be empty")
	private String subject;
	private Map<String, Object> contextVars;

}