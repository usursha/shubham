package com.hpy.ops360.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hpy.ops360.dashboard.entity.HelpDeskHandlerDetails;
import com.hpy.ops360.dashboard.logapi.Loggable;
import com.hpy.ops360.dashboard.repository.HelpDeskHandlerDetailsRepository;

@Service
public class HelpDeskHandlerService {

	@Autowired
	private HelpDeskHandlerDetailsRepository helpDeskHandlerDetailsRepository;

	@Autowired
	private LoginService loginService;

	@Loggable
	public HelpDeskHandlerDetails getHelpDeskHandlerDetails() {

		HelpDeskHandlerDetails handlerDetails = helpDeskHandlerDetailsRepository
				.getHelpDeskHandlerDetails(loginService.getLoggedInUser());
		if (handlerDetails == null) {
			return new HelpDeskHandlerDetails(0L, "", "");
		}
		return handlerDetails;
	}

}
