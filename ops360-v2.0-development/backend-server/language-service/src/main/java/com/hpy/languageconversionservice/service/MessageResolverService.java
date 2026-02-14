package com.hpy.languageconversionservice.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MessageResolverService {
    @Autowired
	private MessageSource messageSource;

//    @Override
	/*
	 * public void setMessageSource(MessageSource messageSource) {
	 * this.messageSource = messageSource; }
	 */

	public Map<String, String> getMessages(Locale locale) {
		Properties properties = ((AnnotationConfigServletWebServerApplicationContext) messageSource)
				.getBean("messageSource", ExposedResourceMessageBundleSource.class).getMessages(locale);
		Map<String, String> messagesMap = new HashMap<>();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			messagesMap.put(entry.getKey().toString(), entry.getValue().toString());
		}
		return messagesMap;
	}
}
