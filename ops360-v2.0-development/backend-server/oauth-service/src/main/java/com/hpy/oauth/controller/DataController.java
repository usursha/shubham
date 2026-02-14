package com.hpy.oauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.oauth.util.MethodUtil;

@RestController
@RequestMapping("/api/v1/data")
public class DataController {

	@Autowired
	private MethodUtil methodUtil;

	@GetMapping("/public")
	public String getPublicData() {
		methodUtil.getLoggedInUserName();
		return "This data is accessible by anyone authenticated.";
	}

	@GetMapping("/admin")
	public String getAdminData() {
		return "This data is only for ADMINs!";
	}

	@GetMapping("/user")
	public String getUserData() {
		return "This data is for USERs and ADMINs!";
	}

	@GetMapping("/specific-permission")
	public String getSpecificPermissionData() {
		return "This data requires VIEW_REPORTS permission!";
	}
}