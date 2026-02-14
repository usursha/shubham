package com.hpy.ops360.report_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.report_service.config.Helper;
import com.hpy.ops360.report_service.dto.DownTicketReportDTO;
import com.hpy.ops360.report_service.dto.DownTicketReportRequestdownload;
import com.hpy.ops360.report_service.entity.DownTicketReportEntityDownload;
import com.hpy.ops360.report_service.repository.DownTicketReportDownloadRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class DownTicketReportServiceDownload {

	@Autowired
	private DownTicketReportDownloadRepository repository;
	
	@Autowired
	private Helper helper;

	public List<DownTicketReportDTO> getReport(DownTicketReportRequestdownload request) {
		
		String user=helper.getLoggedInUser();
		
		log.info("Fetching down ticket report for Manager: {}, CE: {}, Date Range: {} to {}", user,
				 request.getStartDate(), request.getEndDate());


		if (request.getStartDate() == null || request.getStartDate().isEmpty() || request.getEndDate() == null
				|| request.getEndDate().isEmpty()) {
			log.error("Start date and end date are required");
			throw new IllegalArgumentException("Start date and end date are required");
		}

		// Fetching the report data from the repository
		List<DownTicketReportEntityDownload> entities = repository.getDownTicketReportDownload(request.getStartDate(),
				request.getEndDate(), user);

		return entities.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	private DownTicketReportDTO mapToDto(DownTicketReportEntityDownload entity) {
		DownTicketReportDTO dto = new DownTicketReportDTO();
		dto.setAtmid(entity.getAtmid());
		dto.setBusinesscategory(entity.getBusinesscategory());
		dto.setSitetype(entity.getSitetype());
		dto.setBank(entity.getBank());
		dto.setStatus(entity.getStatus());
		dto.setTicketnumber(entity.getTicketnumber());
		dto.setDowntimeinhrs(entity.getDowntimeinhrs());
		dto.setDowntimebucket(entity.getDowntimebucket());
		dto.setVendor(entity.getVendor());
		dto.setOwner(entity.getOwner());
		dto.setSubcalltype(entity.getSubcalltype());
		dto.setCustomerremark(entity.getCustomerremark());
		dto.setCefullname(entity.getCefullname());
		dto.setCmfullname(entity.getCmfullname());
		dto.setCreateddatetime(entity.getCreateddatetime());
		dto.setCeetadatetime(entity.getCeetadatetime());
		dto.setCeactiondatetime(entity.getCeactiondatetime());
		return dto;
	}
}