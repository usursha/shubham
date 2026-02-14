package com.hpy.languageconversionservice.controller;

import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.languageconversionservice.config.HpyLocaleResolver;
import com.hpy.languageconversionservice.dto.LanguageCategoryResponse;
import com.hpy.languageconversionservice.dto.MapResponseDto;
import com.hpy.languageconversionservice.service.LanguageConverterService;
import com.hpy.languageconversionservice.service.MessageResolverService;
import com.hpy.languageconversionservice.service.PortalLanguageConverterService;
import com.hpy.languageconversionservice.util.RestUtilImpl;
import com.hpy.rest.dto.IResponseDto;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/language-service")
@CrossOrigin("${app.cross-origin.allow}")
public class LanguageController {
	
	@Autowired
	private LanguageConverterService service;
	@Autowired
	private MessageResolverService messageResolverService;

	@Autowired
	private HpyLocaleResolver resolver;
	
	@Autowired
	private RestUtilImpl restUtils;
	
	@Autowired
	private PortalLanguageConverterService portalLanguageConverterService;
	
	
	
//	@GetMapping("/{page}")
//	public ResponseEntity<IResponseDto> getLabels(@PathVariable String page, HttpServletRequest request)
//			throws IOException {
//		Locale locale = resolver.resolveLocale(request);
//		MapResponseDto response = new MapResponseDto();
////		Map<String,String> response=service.readProperties(page, locale);
//		response.setMap(service.readProperties(page, locale));
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
//
//	}
	
//	@GetMapping("/{page}")
//	public ResponseEntity<IResponseDto> getPortalLabels(@PathVariable String page, HttpServletRequest request)
//			throws IOException {
//		Locale locale = resolver.resolveLocale(request);
//		MapResponseDto response = new MapResponseDto();
////		Map<String,String> response=service.readProperties(page, locale);
//		response.setMap(portalLanguageConverterService.readPortalProperties(page, locale));
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
//
//	}
	
	
	@GetMapping("/{page}")
	public ResponseEntity<IResponseDto> getPortalLabels(@PathVariable String page, HttpServletRequest request)
	        throws IOException {
	    Locale locale = resolver.resolveLocale(request);
	    MapResponseDto response = new MapResponseDto();
	    response.setMap(portalLanguageConverterService.readPortalProperties(page, locale));
	    return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
	}
	

    @GetMapping("/LanguageList")
    public ResponseEntity<IResponseDto> getLanguages() {
        LanguageCategoryResponse response = portalLanguageConverterService.getLanguages();
	    return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
    }
	
}
