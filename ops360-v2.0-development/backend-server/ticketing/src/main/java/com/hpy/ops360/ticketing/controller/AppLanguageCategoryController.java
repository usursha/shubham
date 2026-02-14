
package com.hpy.ops360.ticketing.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.dto.LanguageCategoryDto;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.service.LanguageCategoryService;
import com.hpy.rest.dto.IResponseDto;
import com.hpy.rest.util.RestUtils;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v2/app/language")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class AppLanguageCategoryController {

	private final LanguageCategoryService languageCategoryService;

	@Autowired
	private RestUtils restUtils;

	@GetMapping("/list")
	@Loggable
	public ResponseEntity<IResponseDto> getActivatedLanguageCategories() {

		List<LanguageCategoryDto> response = languageCategoryService.getActivatedLanguageCategories();
		return ResponseEntity
				.ok(restUtils.wrapResponse(response, "Language List fetched successfully"));

	}

}