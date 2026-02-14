package com.hpy.ops360.dashboard.repository;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hpy.ops360.dashboard.dto.IResponseDtoImpl;
import com.hpy.ops360.dashboard.dto.ProfilePictureDto;


@FeignClient(name = "asset-service", url = "${asset-service.url}")
public interface ProfilePictureFeignClientRepository {
	
	@PostMapping("/save-picture")
	IResponseDtoImpl addProfilePhoto(
			@RequestHeader("Authorization") String authorizationHeader, @RequestBody ProfilePictureDto user);
	
}

