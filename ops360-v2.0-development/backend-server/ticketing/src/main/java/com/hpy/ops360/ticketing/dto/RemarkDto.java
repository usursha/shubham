package com.hpy.ops360.ticketing.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.hpy.ops360.framework.dto.UserLocationDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemarkDto extends UserLocationDto {

	private static final long serialVersionUID = 1302481457900973160L;

	private Long ticketId;

	private RemarkDto parentRemark;

	private String remarks; // alias Internal Remark

	private String remarkBy;

	private String owner;

	private String customerRemark;

	private Integer seqNumber;
	//
	private LocalDate etaDate; // estimation date

	private String resolvedBy; // resolve by time estimation

	private MultipartFile[] file;

	private String createdBy;

	private LocalDateTime createdDate;

	private String lastModifiedBy;

	private LocalDateTime lastModifiedDate;

//	private List<String> attachments;
//
//	private List<RemarkDto> childRemarks;

//	private List<RemarkAssetsDto> remarkAssetsDtos;
}
