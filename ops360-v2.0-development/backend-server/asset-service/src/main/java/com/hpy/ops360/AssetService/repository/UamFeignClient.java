package com.hpy.ops360.AssetService.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hpy.ops360.AssetService.dto.IResponseDtoImpl;
import com.hpy.ops360.AssetService.dto.ProfilePictureDto;
import com.hpy.ops360.AssetService.dto.UserProfilePictureDto;


@FeignClient(name = "user-access-management-service", url = "${user-access-management.url}")
public interface UamFeignClient {


	@PostMapping("/addProfilePhoto")
	IResponseDtoImpl addProfilePhoto(
			@RequestHeader("Authorization") String authorizationHeader, @RequestBody ProfilePictureDto user);
	
	@GetMapping("/profilePicture")
	IResponseDtoImpl getPhoto(@RequestHeader("Authorization") String authorizationHeader);
	
	@PostMapping("/user-profilePicture")
	IResponseDtoImpl getPhotoByUsername(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserProfilePictureDto request);

	
	
}
