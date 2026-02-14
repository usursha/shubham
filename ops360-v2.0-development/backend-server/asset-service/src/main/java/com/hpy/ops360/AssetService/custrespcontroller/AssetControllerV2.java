//package com.hpy.ops360.AssetService.custrespcontroller;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.hpy.ops360.AssetService.dto.AssetRequestDto;
//import com.hpy.ops360.AssetService.dto.AssetResponseDto;
//import com.hpy.ops360.AssetService.dto.HttpResponseDto;
//import com.hpy.ops360.AssetService.entity.Asset;
//import com.hpy.ops360.AssetService.service.AssetService;
//import com.hpy.ops360.AssetService.util.RestUtilsImpl;
//import com.hpy.rest.dto.IResponseDto;
//import com.hpy.rest.dto.ResponseDto;
//
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//
//@RestController
//@Slf4j
//@RequestMapping("/V2/assets")
//public class AssetControllerV2 {
//
//	@Autowired
//	private AssetService assetService;
//
//	@Autowired
//	private RestUtilsImpl restutils;
//
////    @PostMapping("/file")
////    public ResponseEntity<ResponseDto<AssetResponseDto>> convertFileAndSave(@RequestBody @Valid AssetRequestDto assetDto){
////    	ResponseDto<AssetResponseDto> response =new ResponseDto() {};
////    	try {
////    		response.setData(null);
////    		response.setMessage(assetService.convertBase64ToFile(assetDto));
////    		response.setResponseCode(HttpStatus.OK.value());
////    		return ResponseEntity.ok(response);
////    	}catch(Exception e) {
////    		response.setData(null);
////    		response.setMessage("Exception during saving the data : "+ e.getMessage());
////    		response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
////    		return ResponseEntity.ok(response);
////    	}
////    }
//
//	@PostMapping("/file")
//	public ResponseEntity<IResponseDto> convertFileAndSave(@RequestBody @Valid AssetRequestDto assetDto) {
//
//		log.info("**** Inside convertFileAndSave ****");
//		log.info("Request Recieved:- "+ assetDto);
//		String response = assetService.convertBase64ToFile(assetDto);
//		log.info("Response recieved:- "+ response);
//		return ResponseEntity.ok(restutils.wrapResponse(response, "File Converted and saved Successfully"));
//	}
//
////	@PostMapping("/multiplefile")
////	public ResponseEntity<ResponseDto<AssetResponseDto>> convertFileAndSave(
////			@RequestBody List<AssetRequestDto> assetDto) {
////		ResponseDto<AssetResponseDto> response = new ResponseDto() {
////		};
////		try {
////			response.setData(null);
////			response.setMessage(assetService.savemultiplefiles(assetDto));
////			response.setResponseCode(HttpStatus.OK.value());
////			return ResponseEntity.ok(response);
////		} catch (Exception e) {
////			response.setData(null);
////			response.setMessage("Exception during saving the data : " + e.getMessage());
////			response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
////			return ResponseEntity.ok(response);
////		}
////	}
//	
//	@PostMapping("/multiplefile")
//	public ResponseEntity<IResponseDto> convertFileAndSave(
//			@RequestBody List<AssetRequestDto> assetDto) {
//		log.info("**** Inside convertFileAndSave Controller ****");
//		log.info("Request Recieved:- "+ assetDto);
//		
//		String response= assetService.savemultiplefiles(assetDto);
//			
//		log.info("Response Recieved from assetService:- "+ response);
//		return ResponseEntity.ok(restutils.wrapResponse(response, "File Converted and saved Successfully"));
//		
//	}
//	
//	
//
//	@GetMapping("/download/{filename}")
//	public ResponseEntity<IResponseDto> downloadfile(@PathVariable String filename) throws Exception {
//		log.info("**** Inside downloadfile Controller ****");
//		log.info("Request Recieved:- "+ filename);
//		HttpResponseDto response =assetService.getFile(filename);
//		
//		log.info("Response recieved:- "+ response);
//		return ResponseEntity.ok(restutils.wrapResponse(response, "File Downloaded Succesfully"));
//		
//		}
//	}
//
//
