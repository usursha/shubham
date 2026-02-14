package com.hpy.ops360.ticketing.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto extends UserLocationDto {

	private static final long serialVersionUID = 1L;

	private String atmId;
	private String ticketNumber;
	private String owner;
	private String status;
	private String problem; // alias "issue"
	private int expectedTat;
	private int completionTime;
	private MultipartFile[] file;
	private List<RemarkDto> remarks;

}
