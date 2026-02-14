//package com.hpy.languageconversionservice.controller;
//
//import java.io.IOException;
//import java.util.Locale;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.hpy.languageconversionservice.config.HpyLocaleResolver;
//import com.hpy.languageconversionservice.dto.MapResponseDto;
//import com.hpy.languageconversionservice.service.MessageResolverService;
//import com.hpy.languageconversionservice.service.PortalLanguageConverterService;
//import com.hpy.rest.dto.IResponseDto;
//import com.hpy.rest.util.RestUtils;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//@RestController
//@RequestMapping("/v1/portal-language-service")
//@CrossOrigin("${app.cross-origin.allow}")
//public class PortalLanguageController {
//	
//	@Autowired
//	private PortalLanguageConverterService service;
//	@Autowired
//	private MessageResolverService messageResolverService;
//
//	@Autowired
//	private HpyLocaleResolver resolver;
//	
//	@Autowired
//	private RestUtils restUtils;
//	
//	@GetMapping("/{page}")
//	public ResponseEntity<IResponseDto> getLabels(@PathVariable String page, HttpServletRequest request)
//			throws IOException {
//		Locale locale = resolver.resolveLocale(request);
//		MapResponseDto response = new MapResponseDto();
////		Map<String,String> response=service.readProperties(page, locale);
//		response.setMap(service.readPortalProperties(page, locale));
//		return ResponseEntity.ok(restUtils.wrapResponse(response, "OK"));
//
//	}
//}
//
