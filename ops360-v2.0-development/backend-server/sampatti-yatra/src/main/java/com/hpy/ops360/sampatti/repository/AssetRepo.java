package com.hpy.ops360.sampatti.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hpy.ops360.sampatti.dto.IResponseDtoImpl;
import com.hpy.ops360.sampatti.dto.UserProfilePictureDto;

@FeignClient(name = "asset-service", url = "${asset-service.url}")
public interface AssetRepo {
	
	@PostMapping("/get-user-picture")
	IResponseDtoImpl getPhotoByUsername(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserProfilePictureDto request);
	
	@GetMapping("/get-profile-picture-file/{username}")
	ResponseEntity<byte[]> getProfilePicture( @PathVariable String username );
}
