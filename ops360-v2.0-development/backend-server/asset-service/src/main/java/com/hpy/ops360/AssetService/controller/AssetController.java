package com.hpy.ops360.AssetService.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.AssetService.dto.AssetMediaRequestDto;
import com.hpy.ops360.AssetService.dto.AssetMediaTypeResponseDto;
import com.hpy.ops360.AssetService.dto.AssetRequestDto;
import com.hpy.ops360.AssetService.dto.AssetResponseDto;
import com.hpy.ops360.AssetService.dto.DtoAssetRequestWithMultipleFiles;
import com.hpy.ops360.AssetService.dto.HttpResponseDto;
import com.hpy.ops360.AssetService.dto.ImageUrlsList;
import com.hpy.ops360.AssetService.dto.MediaListDto;
import com.hpy.ops360.AssetService.service.AssetService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.dto.ResponseDto;
import com.hpy.rest.util.RestUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/assets")
public class AssetController {
	
    @Autowired
    private AssetService assetService;
    
    @Autowired
    private RestUtils restutil;
   
    
    @PostMapping("/file")
    public ResponseEntity<ResponseDto<AssetResponseDto>> convertFileAndSave(@RequestBody @Valid AssetRequestDto assetDto){
    	ResponseDto<AssetResponseDto> response =new ResponseDto() {};
    	try {
    		response.setData(null);
    		response.setMessage(assetService.convertBase64ToFile(assetDto));
    		response.setResponseCode(HttpStatus.OK.value());
    		return ResponseEntity.ok(response);
    	}catch(Exception e) {
    		response.setData(null);
    		response.setMessage("Exception during saving the data : "+ e.getMessage());
    		response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		return ResponseEntity.ok(response);
    	}
    }
    
    @PostMapping("/get-ticket-images-urls")
    public ResponseEntity<IResponseDto> getImageUrls(@Valid @RequestBody AssetMediaRequestDto request) throws IOException{
    	AssetMediaTypeResponseDto response=assetService.getImageUrls(request);
    	return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
    }
    
//    @PostMapping("/multiplefile")
//    public ResponseEntity<IResponseDto> convertFileAndSave(@Valid @RequestBody MediaListDto assetDto){
//    	String response=assetService.savemultiplefiles(assetDto.getMediaList());
//    	return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
//    }
//   
    @PostMapping("/multiplefile")
    public ResponseEntity<IResponseDto> convertFileAndSave(@Valid @RequestBody MediaListDto assetDto){
    	ImageUrlsList response=assetService.savemultiplefiles(assetDto.getMediaList());
    	return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
    }
   
    
    @GetMapping("/view-ticket-images/{atmId}/{ticketId}/{fileId}")
    public ResponseEntity<byte[]> viewFile(@PathVariable String atmId, @PathVariable String ticketId, @PathVariable String fileId) throws IOException{
    	return assetService.viewFileByName(atmId, ticketId, fileId);
    }
    
    @GetMapping("/download/{filename}")
    public ResponseEntity<ResponseDto<HttpResponseDto>> downloadfile(@PathVariable String filename){
    	ResponseDto<HttpResponseDto> httpResponseDto =new ResponseDto() {};
    	try {
    		httpResponseDto.setMessage("download successfull..");
    		httpResponseDto.setResponseCode(HttpStatus.OK.value());
    		httpResponseDto.setData(assetService.getFile(filename));
    		return ResponseEntity.ok(httpResponseDto);
    	}catch(Exception e) {
    		httpResponseDto.setMessage("download failed : "+e.getMessage());
    		httpResponseDto.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    		httpResponseDto.setData(null);
    		return ResponseEntity.ok(httpResponseDto);
    	}
    }
     
}
