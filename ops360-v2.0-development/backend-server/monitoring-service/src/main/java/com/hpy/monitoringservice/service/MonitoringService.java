package com.hpy.monitoringservice.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hpy.monitoringservice.entity.Monitor;
import com.hpy.monitoringservice.repository.MonitorRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MonitoringService {

	private MonitorRepository monitorRepository;

	private UserAtmDetailsService userAtmDetailsService;

	@Value("${ops360.dashboard.max-atm-list-count}")
	Integer maxAtmListCount;

	public MonitoringService(MonitorRepository monitorRepository, UserAtmDetailsService userAtmDetailsService) {
		super();
		this.monitorRepository = monitorRepository;
		this.userAtmDetailsService = userAtmDetailsService;
	}

	public Map<String, String> getOpenTicketMonitorDetails() {
		Optional<List<Monitor>> monitorResp = monitorRepository.getOpenTicketMonitorDetails();
		Map<String, String> monMap = new LinkedHashMap<>();
		if (monitorResp.isPresent()) {

			monitorResp.get().forEach(monitor -> monMap.put(monitor.getKeyColumn(),
					monitor.getValueData() == null ? "" : monitor.getValueData()));

			return monMap;

		}
		return monMap;
	}

}
