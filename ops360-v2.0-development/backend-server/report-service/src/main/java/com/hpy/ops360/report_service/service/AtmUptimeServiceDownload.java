package com.hpy.ops360.report_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.report_service.config.Helper;
import com.hpy.ops360.report_service.dto.AtmUptimeDTO;
import com.hpy.ops360.report_service.dto.AtmUptimeRequestDownload;
import com.hpy.ops360.report_service.entity.AtmUptimeEntityDownload;
import com.hpy.ops360.report_service.repository.AtmUptimeReportDownloadRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AtmUptimeServiceDownload {

    @Autowired
    private AtmUptimeReportDownloadRepository repository;
    
    @Autowired
	private Helper helper;

    public List<AtmUptimeDTO> getUptimeReport(AtmUptimeRequestDownload request) {
    	String user=helper.getLoggedInUser();
        List<AtmUptimeEntityDownload> entities = repository.getAtmUptimeReportDownload(
        	user,
            request.getStartDate(),
            request.getEndDate()
           
        );
        log.info("Fetched {} records from the database", entities.size());
        log.info("Entiteis::-" + entities);
        List<AtmUptimeDTO> response = entities.stream().map(entity -> {
            AtmUptimeDTO dto = new AtmUptimeDTO();
            dto.setAtmId(entity.getAtmId());
            dto.setBankName(entity.getBankName());
            dto.setCity(entity.getCity());
            dto.setAddress(entity.getAddress());
            dto.setSite(entity.getSite());
            dto.setSiteId(entity.getSiteId());
            dto.setStatus(entity.getStatus());
            dto.setInstallationDate(entity.getInstallationDate());
            dto.setUptime(entity.getUptime());
            dto.setCeFullName(entity.getCeFullName());
            return dto;
        }).collect(Collectors.toList());
        log.info("Mapped entities to DTOs, total DTOs: {}", response.size());
        log.info("Response:-" + response);
        return response;
    }
}
