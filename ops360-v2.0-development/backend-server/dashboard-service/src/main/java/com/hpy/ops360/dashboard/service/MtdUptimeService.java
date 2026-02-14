package com.hpy.ops360.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.entity.MtdUptime;
import com.hpy.ops360.dashboard.repository.CmMtdUptimeRepository;
import com.hpy.ops360.dashboard.repository.MtdUptimeRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MtdUptimeService {

	@Autowired
	private MtdUptimeRepository mtdUptimeRepository;

	@Autowired
	private CmMtdUptimeRepository cmMtdUptimeRepository;

	@Autowired
	private LoginService loginService;

	public MtdUptime getMtdUptimeFromSp(String ceUsername) {
		MtdUptime mtdUptime = mtdUptimeRepository.getMtdUptimeFromSp(ceUsername);
		if (mtdUptime == null) {
			return new MtdUptime(0L, "");
		}
		return mtdUptime;
	}

	public MtdUptime getMtdUptimeFromSp() {
		MtdUptime mtdUptime = mtdUptimeRepository.getMtdUptimeFromSp(loginService.getLoggedInUser());
		if (mtdUptime == null) {
			return new MtdUptime(0L, "");
		}
		return mtdUptime;
	}

}
