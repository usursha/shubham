package com.hpy.oauth.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailDetails {

	@NotEmpty
	private String recipient;
	@NotEmpty
	private String msgBody;
	@NotEmpty
	private String subject;
	@NotEmpty
	private String attachment;
}
