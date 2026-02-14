package com.hpy.languageconversionservice.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class HpyLocaleResolver implements LocaleResolver{

	@Value("${hpy.language.default}")
	private String DEFAULT_LANGUAGE;
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		String language=request.getHeader("Accept-Language");
		if(language==null || language.isEmpty()) {
			return Locale.forLanguageTag(DEFAULT_LANGUAGE);
		}
		Locale locale=Locale.forLanguageTag(language);
		if(LanguageConfig.LOCALES.contains(locale)) {
			return locale;
		}
		return Locale.forLanguageTag(DEFAULT_LANGUAGE);
	}

	
	public Locale getDefaultLocale() {
		return Locale.forLanguageTag(DEFAULT_LANGUAGE);
	}
	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		
		
	}

}
