package com.hpy.languageconversionservice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.hpy.languageconversionservice.config.HpyLocaleResolver;

@Service
public class LanguageConverterService {

	@Autowired
	private  ResourceBundleMessageSource messageSource;
	@Autowired
	private HpyLocaleResolver hpyLocaleResolver;
	@Value("${hpy.language.message-file}")
	private String messageFile;
	
	public LanguageConverterService(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
		this.messageSource.setBasenames("language/messages");
	    this.messageSource.setDefaultEncoding("UTF-8");
	}

	public Map<String, String> readProperties(String page, Locale locale) throws IOException {
//		File file = new ClassPathResource(messageFile).getFile();
		File file = new File(messageFile);
		try (InputStream fis = new FileInputStream(file)) {
			Properties properties = new Properties();
			properties.load(fis);
			Map<String, String> labels = new HashMap<>();
			for (Object keyObj : properties.keySet()) {
				String key = (String) keyObj;
				System.out.println(key);
				if (key.startsWith(page)) {
					try {
						String label = messageSource.getMessage(key, null, locale);
						labels.put(key, label);
					} catch (Exception e) {
						String label = messageSource.getMessage(key, null, hpyLocaleResolver.getDefaultLocale());
						labels.put(key, label);
					}
				}
			}

			return labels;
		} catch (Exception e) {
			System.out.println("Unable to find the specified properties file");
			e.printStackTrace();
			return null;
		}

	}
}
