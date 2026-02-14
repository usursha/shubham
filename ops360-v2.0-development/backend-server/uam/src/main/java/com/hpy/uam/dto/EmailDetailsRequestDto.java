package com.hpy.uam.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailDetailsRequestDto {

	@NotEmpty(message="recipient cannot be blank!!")
	private String recipient;
	
	@NotEmpty(message="msgBody cannot be blank!!")
	private String msgBody;
	
	@NotEmpty(message="subject cannot be blank!!")
	private String subject;
	
	@NotEmpty(message="attachment cannot be blank!!")
	private String attachment;
}
