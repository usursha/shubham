package com.hpy.ops360.sampatti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.sampatti.dto.FinancialYearGroupDto;
import com.hpy.ops360.sampatti.service.AppScoreCardService;
import com.hpy.ops360.sampatti.util.RestUtilsImpl;
import com.hpy.rest.dto.IResponseDto;

@RestController
@RequestMapping("/app/score-card")
@CrossOrigin("${app.cross-origin.allow}")
public class AppScoreCardController {
	

	@Autowired
	private AppScoreCardService scoreCardService;
	
	@Autowired
	private RestUtilsImpl restutil;
	
	@GetMapping("/data")
	public ResponseEntity<IResponseDto> getCEScoreCardData(){
		List<FinancialYearGroupDto> response=scoreCardService.getCEScoreCardData();
		return ResponseEntity.ok(restutil.wrapResponse(response, "success"));
	}

}