package com.hpy.languageconversionservice.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import com.hpy.languageconversionservice.config.HpyLocaleResolver;
import com.hpy.languageconversionservice.dto.LanguageCategoryDTO;
import com.hpy.languageconversionservice.dto.LanguageCategoryResponse;
import com.hpy.languageconversionservice.entity.LanguageListEntity;
import com.hpy.languageconversionservice.repo.LanguageCategoryRepository;

@Service
public class PortalLanguageConverterService {


	@Autowired
	private  ResourceBundleMessageSource messageSource;
	@Autowired
	private HpyLocaleResolver hpyLocaleResolver;
	@Value("${hpy.language.portal-message-file}")
	private String messageFile;
	
	@Autowired
	private LanguageCategoryRepository repository;
	

	
	
	public PortalLanguageConverterService(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
		this.messageSource.setBasenames("language/portal-messages");
	    this.messageSource.setDefaultEncoding("UTF-8");
	}

	public Map<String, String> readPortalProperties(String page, Locale locale) throws IOException {
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
	
	
	


    public LanguageCategoryResponse getLanguages() {
        List<LanguageListEntity> entities = repository.findByActivated(1);
        
        List<LanguageCategoryDTO> languageDTOs = entities.stream()
            .map(entity -> {
                LanguageCategoryDTO dto = new LanguageCategoryDTO();
                dto.setId(entity.getId());
                dto.setLanguageCode(entity.getLanguageCode());
                dto.setLanguageName(entity.getName());
                dto.setTranslation(entity.getLanguageLevel());
                dto.setBgColour(entity.getBgColour());
                dto.setTextColour(entity.getTextColour());
                return dto;
            })
            .collect(Collectors.toList());

        LanguageCategoryResponse response = new LanguageCategoryResponse();
        response.setSuggestedLanguage(languageDTOs);
        response.setAll(languageDTOs);

        return response;
    }
}



