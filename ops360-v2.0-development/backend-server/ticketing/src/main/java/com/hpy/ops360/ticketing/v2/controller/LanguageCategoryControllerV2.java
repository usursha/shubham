package com.hpy.ops360.ticketing.v2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.ops360.ticketing.dto.LanguageCategoryDto;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.service.LanguageCategoryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v2/language")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class LanguageCategoryControllerV2 {

	private final LanguageCategoryService languageCategoryService;

	@GetMapping("/list")
	@Loggable
	public ResponseEntity<List<LanguageCategoryDto>> getActivatedLanguageCategories() {

		return ResponseEntity.ok(languageCategoryService.getActivatedLanguageCategories());

	}

}