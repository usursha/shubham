package com.hpy.ops360.ticketing.v2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.generic.Exception.EntityNotFoundException;
import com.hpy.ops360.ticketing.dto.FilterCatgoryDto;
import com.hpy.ops360.ticketing.logapi.Loggable;
import com.hpy.ops360.ticketing.service.FilterCategoryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v2/filter")
@AllArgsConstructor
@CrossOrigin("${app.cross-origin.allow}")
public class FilterControllerV2 {

	private FilterCategoryService filterCategoryService;

	@GetMapping("/{filterCategoryId}")
	@Loggable
	public ResponseEntity<FilterCatgoryDto> getFilterCategoryById(@PathVariable Long filterCategoryId) {
		try {
			return ResponseEntity.ok(filterCategoryService.getFilterCategoryById(filterCategoryId));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}

	}
}