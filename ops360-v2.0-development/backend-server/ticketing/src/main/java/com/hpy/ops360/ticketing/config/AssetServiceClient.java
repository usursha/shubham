package com.hpy.ops360.ticketing.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.hpy.ops360.ticketing.cm.dto.ImageSaveRequest;
import com.hpy.ops360.ticketing.cm.dto.ImageSaveResponse;

@FeignClient(name = "assetServiceClient", url = "${asset.service.url}")
public interface AssetServiceClient {

	@PostMapping("/image/save-images")
	ImageSaveResponse saveImages(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
			@RequestBody ImageSaveRequest request);

//	@PostMapping("/assets/multiplefile")
//	IResponseDtoImpl convertFileAndSave(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
//			@RequestBody MediaListDto mediaListDto);
//	
//	@PostMapping("/assets/get-ticket-images-urls")
//	IResponseDtoImplurl getImageUrls(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
//			@RequestBody AssetMediaRequestDto request);
}
