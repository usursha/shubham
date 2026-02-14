package com.hpy.ops360.AssetService.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.AssetService.dto.IResponseDtoImpl;
import com.hpy.ops360.AssetService.dto.ProfilePictureRequestDto;
import com.hpy.ops360.AssetService.dto.UserProfilePictureDto;
import com.hpy.ops360.AssetService.service.UamFeignService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/feign-client")
public class UamFeignController {

	@Autowired
	private UamFeignService uamfeignservice;

	
	@PostMapping("/save-picture")
	public IResponseDtoImpl getProfilePhoto(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token,@Valid @RequestBody ProfilePictureRequestDto assetDto) throws MalformedURLException, IOException {
		IResponseDtoImpl data=uamfeignservice.savePicture(token, assetDto);
		return data;
	}
	
	@GetMapping("/get-picture")
	public IResponseDtoImpl getProfilePhoto(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws MalformedURLException, IOException {
		IResponseDtoImpl data=uamfeignservice.fetchProfilePicture(token);
		return data;
	}
	
	@PostMapping("/get-user-picture")
	public IResponseDtoImpl getProfilePhotoByUsername(
			@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody UserProfilePictureDto request) throws MalformedURLException, IOException {
		IResponseDtoImpl data=uamfeignservice.fetchProfilePictureByUsername(token, request);
		return data;
	}
	
	@GetMapping("/get-profile-picture-file/{username}")
	public ResponseEntity<byte[]> viewFile(@PathVariable String username)throws IOException{
		return uamfeignservice.viewFile(username);
	}

}
