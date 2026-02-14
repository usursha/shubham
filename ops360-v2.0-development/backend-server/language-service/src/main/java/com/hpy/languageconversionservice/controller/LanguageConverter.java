package com.hpy.languageconversionservice.controller;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hpy.languageconversionservice.config.HpyLocaleResolver;
import com.hpy.languageconversionservice.dto.MapResponseDto;
import com.hpy.languageconversionservice.service.LanguageConverterService;
import com.hpy.languageconversionservice.service.MessageResolverService;
import com.hpy.languageconversionservice.util.RestUtilImpl;
import com.hpy.rest.dto.IResponseDto;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/language-service")
@CrossOrigin("${app.cross-origin.allow}")
public class LanguageConverter {

	@Autowired
	private LanguageConverterService service;
	@Autowired
	private MessageResolverService messageResolverService;

	@Autowired
	private HpyLocaleResolver resolver;
	
	@Autowired
	private RestUtilImpl restUtils;

	@GetMapping("/{page}")
	public Map<String, String> getLabelsWorking(@PathVariable String page, HttpServletRequest request)
			throws IOException {
		Locale locale = resolver.resolveLocale(request);
		return service.readProperties(page, locale);
		// return messageSource.getMessage("hello", null,locale);
	}

//	@GetMapping("/underTesting/{page}")
//	public Map<String, String> getLabels(@PathVariable String page, HttpServletRequest request) throws IOException {
//		Locale locale = resolver.resolveLocale(request);
//		return messageResolverService.getMessages(locale);
//		// return messageSource.getMessage("hello", null,locale);
//	}


}
